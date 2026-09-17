package com.example.player

data class PlayerState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = true,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedPositionMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val isMuted: Boolean = false,
    val currentVolume: Float = 1.0f, // 0.0 to 1.0
    val isLocked: Boolean = false,
    val areControlsVisible: Boolean = true,
    val isFullscreen: Boolean = true,
    // Resolution
    val selectedResolution: String = "Auto (Adaptive)",
    val availableResolutions: List<String> = emptyList(),
    // Audio tracks
    val selectedAudioTrack: String = "Default Audio",
    val availableAudioTracks: List<String> = emptyList(),
    // Subtitles
    val isSubtitlesEnabled: Boolean = false,
    val selectedSubtitleTrack: String = "None",
    val availableSubtitleTracks: List<String> = emptyList(),
    // Gesture feedback HUD
    val hudGestureType: GestureFeedbackType = GestureFeedbackType.NONE,
    val hudValue: Float = 0f, // e.g. 0 to 100
    val hudSeekOffsetSeconds: Int = 0,
    val errorMessage: String? = null
)

enum class GestureFeedbackType {
    NONE,
    BRIGHTNESS,
    VOLUME,
    SEEK
}
