package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val movieId: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseYear: Int,
    val rating: Double,
    val genres: String, // comma-separated
    val runtimeMinutes: Int,
    val resolutionBadge: String,
    val isWatched: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)
