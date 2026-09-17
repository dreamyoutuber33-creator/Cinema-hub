package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PlayerSettings
import com.example.data.local.QualityPreference
import com.example.data.local.UserPreferencesRepository
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    private val context: Context,
    private val preferencesRepository: UserPreferencesRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    val settings: StateFlow<PlayerSettings> = preferencesRepository.settings

    private val _cacheSizeMb = MutableStateFlow("Calculating...")
    val cacheSizeMb: StateFlow<String> = _cacheSizeMb.asStateFlow()

    init {
        calculateCacheSize()
    }

    fun updateSettings(transform: (PlayerSettings) -> PlayerSettings) {
        preferencesRepository.updateSettings(transform)
    }

    fun calculateCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            val cacheDir = context.cacheDir
            val sizeBytes = getFolderSize(cacheDir)
            val sizeMb = String.format("%.1f MB", sizeBytes / (1024.0 * 1024.0))
            _cacheSizeMb.value = sizeMb
        }
    }

    private fun getFolderSize(file: File?): Long {
        if (file == null || !file.exists()) return 0L
        if (!file.isDirectory) return file.length()
        var total = 0L
        file.listFiles()?.forEach { child ->
            total += getFolderSize(child)
        }
        return total
    }

    fun clearImageCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageCache = File(context.cacheDir, "image_cache")
            if (imageCache.exists()) imageCache.deleteRecursively()
            calculateCacheSize()
        }
    }

    fun clearVideoCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val videoCache = File(context.cacheDir, "media")
            if (videoCache.exists()) videoCache.deleteRecursively()
            calculateCacheSize()
        }
    }

    fun clearAllCache() {
        viewModelScope.launch(Dispatchers.IO) {
            context.cacheDir.deleteRecursively()
            calculateCacheSize()
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            movieRepository.clearSearchHistory()
        }
    }

    fun clearWatchHistory() {
        viewModelScope.launch {
            movieRepository.clearHistory()
        }
    }
}
