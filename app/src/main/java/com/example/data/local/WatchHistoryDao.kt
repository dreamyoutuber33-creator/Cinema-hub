package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY lastWatchedTime DESC")
    fun getAllHistory(): Flow<List<WatchHistoryEntity>>

    // Unfinished videos: watchedPercentage between 1 and 94%
    @Query("SELECT * FROM watch_history WHERE watchedPercentage > 1 AND watchedPercentage < 95 ORDER BY lastWatchedTime DESC")
    fun getContinueWatching(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE movieId = :movieId LIMIT 1")
    suspend fun getHistoryByMovieId(movieId: String): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(history: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: String)

    @Query("DELETE FROM watch_history")
    suspend fun clearAll()
}
