package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

enum class DownloadStatus {
    QUEUED, DOWNLOADING, PAUSED, COMPLETED, CANCELLED, FAILED
}

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey
    val movieId: String,
    val title: String,
    val posterUrl: String,
    val downloadUrl: String,
    val localFilePath: String,
    val resolution: String,
    val totalBytes: Long = 0,
    val downloadedBytes: Long = 0,
    val progress: Int = 0, // 0 to 100
    val status: String = DownloadStatus.QUEUED.name,
    val addedAt: Long = System.currentTimeMillis()
)

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY addedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM downloads WHERE movieId = :movieId LIMIT 1")
    suspend fun getDownload(movieId: String): DownloadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(download: DownloadEntity)

    @Query("UPDATE downloads SET progress = :progress, downloadedBytes = :downloadedBytes, status = :status WHERE movieId = :movieId")
    suspend fun updateProgress(movieId: String, progress: Int, downloadedBytes: Long, status: String)

    @Query("UPDATE downloads SET status = :status WHERE movieId = :movieId")
    suspend fun updateStatus(movieId: String, status: String)

    @Query("DELETE FROM downloads WHERE movieId = :movieId")
    suspend fun deleteDownload(movieId: String)
}
