package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class QualityPreference(val label: String) {
    AUTO("Auto (Adaptive)"),
    HIGHEST("Always Highest (4K/1080p)"),
    FHD("1080p Full HD"),
    HD("720p HD"),
    SD("480p Standard"),
    DATA_SAVER("Data Saver (360p)")
}

data class PlayerSettings(
    val autoPlay: Boolean = true,
    val defaultQuality: QualityPreference = QualityPreference.AUTO,
    val wifiQuality: QualityPreference = QualityPreference.HIGHEST,
    val mobileQuality: QualityPreference = QualityPreference.HD,
    val defaultPlaybackSpeed: Float = 1.0f,
    val resumePlayback: Boolean = true,
    val keepScreenAwake: Boolean = true,
    val autoFullscreen: Boolean = true,
    val pipEnabled: Boolean = true,
    val gesturesEnabled: Boolean = true,
    val doubleTapSeekSeconds: Int = 10,
    val controlsTimeoutSeconds: Int = 4,
    // Subtitles
    val subtitlesEnabled: Boolean = true,
    val preferredSubtitleLanguage: String = "English",
    val subtitleTextSize: String = "Medium", // Small, Medium, Large, Extra Large
    val subtitleTextColor: String = "White", // White, Yellow, Cyan
    val subtitleBackground: Boolean = true,
    // Audio
    val preferredAudioLanguage: String = "English",
    val audioNormalization: Boolean = true,
    // Appearance & Data
    val isAmoledDark: Boolean = true,
    val accentColor: String = "Gold", // Gold, Crimson, Indigo, Emerald
    val dataSaverMode: Boolean = false,
    val wifiOnlyStreaming: Boolean = false,
    val askBeforeMobileData: Boolean = false,
    val tmdbApiKey: String = ""
)

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cinemahub_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<PlayerSettings> = _settings.asStateFlow()

    private fun loadSettings(): PlayerSettings {
        return PlayerSettings(
            autoPlay = prefs.getBoolean("auto_play", true),
            defaultQuality = QualityPreference.valueOf(prefs.getString("default_quality", QualityPreference.AUTO.name) ?: QualityPreference.AUTO.name),
            wifiQuality = QualityPreference.valueOf(prefs.getString("wifi_quality", QualityPreference.HIGHEST.name) ?: QualityPreference.HIGHEST.name),
            mobileQuality = QualityPreference.valueOf(prefs.getString("mobile_quality", QualityPreference.HD.name) ?: QualityPreference.HD.name),
            defaultPlaybackSpeed = prefs.getFloat("playback_speed", 1.0f),
            resumePlayback = prefs.getBoolean("resume_playback", true),
            keepScreenAwake = prefs.getBoolean("keep_screen_awake", true),
            autoFullscreen = prefs.getBoolean("auto_fullscreen", true),
            pipEnabled = prefs.getBoolean("pip_enabled", true),
            gesturesEnabled = prefs.getBoolean("gestures_enabled", true),
            doubleTapSeekSeconds = prefs.getInt("seek_seconds", 10),
            controlsTimeoutSeconds = prefs.getInt("controls_timeout", 4),
            subtitlesEnabled = prefs.getBoolean("subtitles_enabled", true),
            preferredSubtitleLanguage = prefs.getString("subtitle_lang", "English") ?: "English",
            subtitleTextSize = prefs.getString("subtitle_size", "Medium") ?: "Medium",
            subtitleTextColor = prefs.getString("subtitle_color", "White") ?: "White",
            subtitleBackground = prefs.getBoolean("subtitle_bg", true),
            preferredAudioLanguage = prefs.getString("audio_lang", "English") ?: "English",
            audioNormalization = prefs.getBoolean("audio_normalization", true),
            isAmoledDark = prefs.getBoolean("amoled_dark", true),
            accentColor = prefs.getString("accent_color", "Gold") ?: "Gold",
            dataSaverMode = prefs.getBoolean("data_saver", false),
            wifiOnlyStreaming = prefs.getBoolean("wifi_only", false),
            askBeforeMobileData = prefs.getBoolean("ask_mobile_data", false),
            tmdbApiKey = prefs.getString("tmdb_api_key", "") ?: ""
        )
    }

    fun updateSettings(transform: (PlayerSettings) -> PlayerSettings) {
        val updated = transform(_settings.value)
        _settings.value = updated
        prefs.edit().apply {
            putBoolean("auto_play", updated.autoPlay)
            putString("default_quality", updated.defaultQuality.name)
            putString("wifi_quality", updated.wifiQuality.name)
            putString("mobile_quality", updated.mobileQuality.name)
            putFloat("playback_speed", updated.defaultPlaybackSpeed)
            putBoolean("resume_playback", updated.resumePlayback)
            putBoolean("keep_screen_awake", updated.keepScreenAwake)
            putBoolean("auto_fullscreen", updated.autoFullscreen)
            putBoolean("pip_enabled", updated.pipEnabled)
            putBoolean("gestures_enabled", updated.gesturesEnabled)
            putInt("seek_seconds", updated.doubleTapSeekSeconds)
            putInt("controls_timeout", updated.controlsTimeoutSeconds)
            putBoolean("subtitles_enabled", updated.subtitlesEnabled)
            putString("subtitle_lang", updated.preferredSubtitleLanguage)
            putString("subtitle_size", updated.subtitleTextSize)
            putString("subtitle_color", updated.subtitleTextColor)
            putBoolean("subtitle_bg", updated.subtitleBackground)
            putString("audio_lang", updated.preferredAudioLanguage)
            putBoolean("audio_normalization", updated.audioNormalization)
            putBoolean("amoled_dark", updated.isAmoledDark)
            putString("accent_color", updated.accentColor)
            putBoolean("data_saver", updated.dataSaverMode)
            putBoolean("wifi_only", updated.wifiOnlyStreaming)
            putBoolean("ask_mobile_data", updated.askBeforeMobileData)
            putString("tmdb_api_key", updated.tmdbApiKey)
            apply()
        }
    }
}
