package com.example.data.model

data class Movie(
    val id: String,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseYear: Int,
    val releaseDate: String = "$releaseYear",
    val rating: Double,
    val voteCount: Int = 1250,
    val runtimeMinutes: Int,
    val genres: List<String>,
    val resolutionBadge: String = "4K HDR",
    val language: String = "English",
    val country: String = "United States",
    val director: String = "Director",
    val cast: List<CastMember> = emptyList(),
    val production: String = "Studio Production",
    val trailerUrl: String = "",
    val videoSources: List<VideoResolutionSource> = emptyList(),
    val isFeatured: Boolean = false
)

data class CastMember(
    val name: String,
    val character: String,
    val avatarUrl: String
)

data class VideoResolutionSource(
    val label: String, // e.g. "Auto", "2160p 4K", "1080p FHD", "720p HD", "480p SD", "360p"
    val resolutionWidth: Int,
    val resolutionHeight: Int,
    val bitrate: Long,
    val url: String
)

data class AudioTrackOption(
    val id: String,
    val language: String,
    val label: String,
    val channels: String = "Stereo 2.0"
)

data class SubtitleTrackOption(
    val id: String,
    val language: String,
    val label: String,
    val url: String = ""
)
