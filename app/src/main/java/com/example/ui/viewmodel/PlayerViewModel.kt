package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import com.example.player.CinemaPlayerManager
import com.example.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val context: Context,
    private val movieRepository: MovieRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val playerManager = CinemaPlayerManager(context, viewModelScope)
    val playerState: StateFlow<PlayerState> = playerManager.playerState

    private var currentMovie: Movie? = null
    private var progressSaveJob: Job? = null

    fun playMovie(movieId: String) {
        val movie = movieRepository.getMovieById(movieId) ?: return
        currentMovie = movie

        viewModelScope.launch {
            val settings = preferencesRepository.settings.value
            val history = movieRepository.getHistoryItem(movieId)

            val resumePos = if (settings.resumePlayback && history != null) {
                // If within last 95% of video, resume. Otherwise restart
                if (history.watchedPercentage < 95) history.lastPositionMs else 0L
            } else 0L

            val preferredQuality = settings.defaultQuality.label

            playerManager.playMovie(
                movie = movie,
                initialPositionMs = resumePos,
                preferredSpeed = settings.defaultPlaybackSpeed,
                preferredQuality = preferredQuality
            )

            // Periodic playback progress saving to Room
            startProgressSaving(movie)
        }
    }

    fun playCustomStream(url: String, title: String) {
        playerManager.playCustomUrl(url, title)
    }

    private fun startProgressSaving(movie: Movie) {
        progressSaveJob?.cancel()
        progressSaveJob = viewModelScope.launch {
            while (isActive) {
                delay(4000)
                val state = playerState.value
                if (state.durationMs > 0 && state.currentPositionMs > 1000) {
                    movieRepository.savePlaybackProgress(
                        movie = movie,
                        positionMs = state.currentPositionMs,
                        durationMs = state.durationMs,
                        streamUrl = movie.videoSources.firstOrNull()?.url ?: movie.trailerUrl
                    )
                }
            }
        }
    }

    fun togglePlayPause() = playerManager.togglePlayPause()

    fun seekTo(positionMs: Long) = playerManager.seekTo(positionMs)

    fun seekBy(seconds: Int) = playerManager.seekBy(seconds)

    fun setPlaybackSpeed(speed: Float) = playerManager.setPlaybackSpeed(speed)

    fun setVolume(volumeFraction: Float) = playerManager.setVolume(volumeFraction)

    fun toggleMute() = playerManager.toggleMute()

    fun switchResolution(res: String) = playerManager.switchResolution(res)

    fun selectAudioTrack(track: String) = playerManager.selectAudioTrack(track)

    fun toggleSubtitles(enabled: Boolean, track: String = "English") = playerManager.setSubtitlesEnabled(enabled, track)

    fun toggleLock() = playerManager.toggleLock()

    fun toggleControls() {
        val timeout = preferencesRepository.settings.value.controlsTimeoutSeconds
        playerManager.toggleControlsVisibility(timeout)
    }

    fun setControlsVisible(visible: Boolean) {
        val timeout = preferencesRepository.settings.value.controlsTimeoutSeconds
        playerManager.setControlsVisible(visible, timeout)
    }

    fun showBrightnessHud(percent: Int) = playerManager.showBrightnessHud(percent)

    fun showVolumeHud(percent: Int) = playerManager.showVolumeHud(percent)

    override fun onCleared() {
        super.onCleared()
        // Save final progress
        val movie = currentMovie
        val state = playerState.value
        if (movie != null && state.durationMs > 0) {
            viewModelScope.launch {
                movieRepository.savePlaybackProgress(
                    movie = movie,
                    positionMs = state.currentPositionMs,
                    durationMs = state.durationMs,
                    streamUrl = movie.videoSources.firstOrNull()?.url ?: movie.trailerUrl
                )
            }
        }
        progressSaveJob?.cancel()
        playerManager.release()
    }
}
