package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey
    val movieId: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val videoUrl: String,
    val lastPositionMs: Long,
    val totalDurationMs: Long,
    val watchedPercentage: Int, // 0 to 100
    val lastWatchedTime: Long = System.currentTimeMillis()
)
