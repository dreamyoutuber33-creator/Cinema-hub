package com.example.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.model.Movie
import com.example.data.model.VideoResolutionSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class CinemaPlayerManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private var exoPlayer: ExoPlayer? = null
    private var progressJob: Job? = null
    private var hudDismissJob: Job? = null
    private var controlsDismissJob: Job? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var currentMovie: Movie? = null
    private var currentSourceUrl: String = ""

    fun initializePlayer(): ExoPlayer {
        if (exoPlayer == null) {
            val player = ExoPlayer.Builder(context).build()
            player.addListener(playerEventListener)
            exoPlayer = player
        }
        return exoPlayer!!
    }

    fun getPlayer(): ExoPlayer? = exoPlayer

    fun playMovie(
        movie: Movie,
        initialPositionMs: Long = 0L,
        preferredSpeed: Float = 1.0f,
        preferredQuality: String = "Auto (Adaptive)"
    ) {
        val player = initializePlayer()
        currentMovie = movie

        // Select source
        val source = movie.videoSources.find { it.label == preferredQuality }
            ?: movie.videoSources.firstOrNull()
        val streamUrl = source?.url ?: movie.trailerUrl

        currentSourceUrl = streamUrl

        val availableResolutions = if (movie.videoSources.isNotEmpty()) {
            movie.videoSources.map { it.label }
        } else {
            listOf("Auto (Adaptive)", "1080p FHD", "720p HD", "480p SD")
        }

        _playerState.update {
            it.copy(
                selectedResolution = source?.label ?: "Auto (Adaptive)",
                availableResolutions = availableResolutions,
                playbackSpeed = preferredSpeed,
                isBuffering = true,
                errorMessage = null
            )
        }

        val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))
        player.setMediaItem(mediaItem)
        player.playbackParameters = PlaybackParameters(preferredSpeed)
        player.prepare()

        if (initialPositionMs > 0) {
            player.seekTo(initialPositionMs)
        }

        player.playWhenReady = true
        startProgressTracking()
        startControlsTimeout(4)
    }

    fun playCustomUrl(url: String, title: String) {
        val player = initializePlayer()
        currentSourceUrl = url

        _playerState.update {
            it.copy(
                selectedResolution = "Auto (Adaptive)",
                availableResolutions = listOf("Auto (Adaptive)", "Source Stream"),
                isBuffering = true,
                errorMessage = null
            )
        }

        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        startProgressTracking()
        startControlsTimeout(4)
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(positionMs: Long) {
        val player = exoPlayer ?: return
        val clamped = positionMs.coerceIn(0L, player.duration.coerceAtLeast(0L))
        player.seekTo(clamped)
        _playerState.update { it.copy(currentPositionMs = clamped) }
    }

    fun seekBy(seconds: Int) {
        val player = exoPlayer ?: return
        val target = player.currentPosition + (seconds * 1000L)
        seekTo(target)
        showSeekHud(seconds)
    }

    fun setPlaybackSpeed(speed: Float) {
        val player = exoPlayer ?: return
        player.playbackParameters = PlaybackParameters(speed)
        _playerState.update { it.copy(playbackSpeed = speed) }
    }

    fun setVolume(volumeFraction: Float) {
        val player = exoPlayer ?: return
        val vol = volumeFraction.coerceIn(0f, 1f)
        player.volume = vol
        _playerState.update {
            it.copy(
                currentVolume = vol,
                isMuted = vol == 0f
            )
        }
        showVolumeHud((vol * 100).toInt())
    }

    fun toggleMute() {
        val player = exoPlayer ?: return
        val currentlyMuted = _playerState.value.isMuted
        if (currentlyMuted) {
            val prevVol = if (_playerState.value.currentVolume > 0f) _playerState.value.currentVolume else 0.8f
            player.volume = prevVol
            _playerState.update { it.copy(isMuted = false, currentVolume = prevVol) }
            showVolumeHud((prevVol * 100).toInt())
        } else {
            player.volume = 0f
            _playerState.update { it.copy(isMuted = true) }
            showVolumeHud(0)
        }
    }

    fun switchResolution(resolutionLabel: String) {
        val movie = currentMovie ?: return
        val player = exoPlayer ?: return

        val matchedSource = movie.videoSources.find { it.label == resolutionLabel }
        if (matchedSource != null && matchedSource.url != currentSourceUrl) {
            val currentPos = player.currentPosition
            val isPlaying = player.isPlaying
            currentSourceUrl = matchedSource.url

            val mediaItem = MediaItem.fromUri(Uri.parse(matchedSource.url))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.seekTo(currentPos)
            player.playWhenReady = isPlaying
        }

        _playerState.update { it.copy(selectedResolution = resolutionLabel) }
    }

    fun selectAudioTrack(trackName: String) {
        _playerState.update { it.copy(selectedAudioTrack = trackName) }
    }

    fun setSubtitlesEnabled(enabled: Boolean, trackName: String = "English") {
        val player = exoPlayer ?: return
        val trackParams = player.trackSelectionParameters.buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, !enabled)
            .build()
        player.trackSelectionParameters = trackParams

        _playerState.update {
            it.copy(
                isSubtitlesEnabled = enabled,
                selectedSubtitleTrack = if (enabled) trackName else "None"
            )
        }
    }

    fun toggleLock() {
        _playerState.update {
            val newLocked = !it.isLocked
            it.copy(
                isLocked = newLocked,
                areControlsVisible = !newLocked
            )
        }
    }

    fun toggleControlsVisibility(timeoutSec: Int = 4) {
        if (_playerState.value.isLocked) return
        _playerState.update { it.copy(areControlsVisible = !it.areControlsVisible) }
        if (_playerState.value.areControlsVisible) {
            startControlsTimeout(timeoutSec)
        } else {
            controlsDismissJob?.cancel()
        }
    }

    fun setControlsVisible(visible: Boolean, timeoutSec: Int = 4) {
        if (_playerState.value.isLocked && visible) return
        _playerState.update { it.copy(areControlsVisible = visible) }
        if (visible) {
            startControlsTimeout(timeoutSec)
        } else {
            controlsDismissJob?.cancel()
        }
    }

    fun startControlsTimeout(seconds: Int) {
        controlsDismissJob?.cancel()
        if (seconds <= 0) return
        controlsDismissJob = coroutineScope.launch {
            delay(seconds * 1000L)
            if (isActive && _playerState.value.isPlaying && !_playerState.value.isLocked) {
                _playerState.update { it.copy(areControlsVisible = false) }
            }
        }
    }

    fun showBrightnessHud(percent: Int) {
        hudDismissJob?.cancel()
        _playerState.update {
            it.copy(
                hudGestureType = GestureFeedbackType.BRIGHTNESS,
                hudValue = percent.toFloat()
            )
        }
        hudDismissJob = coroutineScope.launch {
            delay(1200)
            if (isActive) {
                _playerState.update { it.copy(hudGestureType = GestureFeedbackType.NONE) }
            }
        }
    }

    fun showVolumeHud(percent: Int) {
        hudDismissJob?.cancel()
        _playerState.update {
            it.copy(
                hudGestureType = GestureFeedbackType.VOLUME,
                hudValue = percent.toFloat()
            )
        }
        hudDismissJob = coroutineScope.launch {
            delay(1200)
            if (isActive) {
                _playerState.update { it.copy(hudGestureType = GestureFeedbackType.NONE) }
            }
        }
    }

    private fun showSeekHud(offsetSec: Int) {
        hudDismissJob?.cancel()
        _playerState.update {
            it.copy(
                hudGestureType = GestureFeedbackType.SEEK,
                hudSeekOffsetSeconds = offsetSec
            )
        }
        hudDismissJob = coroutineScope.launch {
            delay(1200)
            if (isActive) {
                _playerState.update { it.copy(hudGestureType = GestureFeedbackType.NONE) }
            }
        }
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = coroutineScope.launch(Dispatchers.Main) {
            while (isActive) {
                val player = exoPlayer
                if (player != null) {
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    val buf = player.bufferedPosition.coerceAtLeast(0L)

                    _playerState.update {
                        it.copy(
                            currentPositionMs = pos,
                            durationMs = dur,
                            bufferedPositionMs = buf,
                            isPlaying = player.isPlaying
                        )
                    }
                }
                delay(300)
            }
        }
    }

    private val playerEventListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    _playerState.update { it.copy(isBuffering = true) }
                }
                Player.STATE_READY -> {
                    val duration = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L
                    _playerState.update {
                        it.copy(
                            isBuffering = false,
                            durationMs = duration
                        )
                    }
                }
                Player.STATE_ENDED -> {
                    _playerState.update {
                        it.copy(
                            isPlaying = false,
                            areControlsVisible = true
                        )
                    }
                }
                Player.STATE_IDLE -> {
                    _playerState.update { it.copy(isBuffering = false) }
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playerState.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) {
                startControlsTimeout(4)
            } else {
                controlsDismissJob?.cancel()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _playerState.update {
                it.copy(
                    isBuffering = false,
                    isPlaying = false,
                    errorMessage = "Playback error: ${error.localizedMessage ?: "Unable to stream source"}"
                )
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            extractTracks(tracks)
        }
    }

    private fun extractTracks(tracks: Tracks) {
        val audioTracks = mutableListOf<String>()
        val subtitleTracks = mutableListOf<String>()

        for (trackGroup in tracks.groups) {
            val type = trackGroup.type
            val mediaGroup = trackGroup.mediaTrackGroup

            for (i in 0 until mediaGroup.length) {
                val format = mediaGroup.getFormat(i)
                when (type) {
                    C.TRACK_TYPE_AUDIO -> {
                        val lang = format.language ?: "Original Audio"
                        val channels = if (format.channelCount > 2) "5.1 Surround" else "Stereo 2.0"
                        audioTracks.add("$lang ($channels)")
                    }
                    C.TRACK_TYPE_TEXT -> {
                        val lang = format.language ?: "English"
                        subtitleTracks.add(lang)
                    }
                }
            }
        }

        val finalAudio = if (audioTracks.isNotEmpty()) audioTracks.distinct() else listOf("Original Audio", "English (Stereo 2.0)", "Director Commentary")
        val finalSubs = if (subtitleTracks.isNotEmpty()) subtitleTracks.distinct() else listOf("English", "Spanish", "French", "German")

        _playerState.update {
            it.copy(
                availableAudioTracks = finalAudio,
                availableSubtitleTracks = finalSubs
            )
        }
    }

    fun release() {
        progressJob?.cancel()
        hudDismissJob?.cancel()
        controlsDismissJob?.cancel()
        exoPlayer?.removeListener(playerEventListener)
        exoPlayer?.release()
        exoPlayer = null
    }
}
