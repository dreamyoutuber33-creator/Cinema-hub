package com.example.data.repository

import com.example.data.local.CinemaDatabase
import com.example.data.local.DownloadDao
import com.example.data.local.DownloadEntity
import com.example.data.local.SearchHistoryDao
import com.example.data.local.SearchHistoryEntity
import com.example.data.local.WatchHistoryDao
import com.example.data.local.WatchHistoryEntity
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale

class MovieRepository(
    private val database: CinemaDatabase
) {
    private val watchlistDao: WatchlistDao = database.watchlistDao()
    private val watchHistoryDao: WatchHistoryDao = database.watchHistoryDao()
    private val searchHistoryDao: SearchHistoryDao = database.searchHistoryDao()
    private val downloadDao: DownloadDao = database.downloadDao()

    // In-memory catalog supplemented by legal data source
    private val allMovies: List<Movie> = LegalMoviesDataSource.movies

    fun getAllMovies(): List<Movie> = allMovies

    fun getMovieById(id: String): Movie? {
        return allMovies.find { it.id == id }
    }

    fun getFeaturedMovies(): List<Movie> {
        return allMovies.filter { it.isFeatured }
    }

    fun getTrendingMovies(): List<Movie> {
        return allMovies.sortedByDescending { it.voteCount }
    }

    fun getPopularMovies(): List<Movie> {
        return allMovies.sortedByDescending { it.rating * it.voteCount }
    }

    fun getNowPlayingMovies(): List<Movie> {
        return allMovies.filter { it.releaseYear >= 2015 }
    }

    fun getTopRatedMovies(): List<Movie> {
        return allMovies.sortedByDescending { it.rating }
    }

    fun getUpcomingMovies(): List<Movie> {
        return allMovies.sortedByDescending { it.releaseYear }
    }

    fun getRecommendedMovies(currentMovieId: String? = null): List<Movie> {
        return allMovies.filter { it.id != currentMovieId }.shuffled().take(6)
    }

    fun getMoviesByGenre(genre: String): List<Movie> {
        if (genre.equals("All", ignoreCase = true)) return allMovies
        return allMovies.filter { movie ->
            movie.genres.any { it.equals(genre, ignoreCase = true) }
        }
    }

    fun searchMovies(
        query: String,
        genreFilter: String? = null,
        yearFilter: Int? = null,
        minRating: Double? = null,
        sortBy: String = "Popularity"
    ): List<Movie> {
        val q = query.trim().lowercase(Locale.ROOT)
        var results = allMovies.filter { movie ->
            val matchesQuery = if (q.isEmpty()) true else {
                movie.title.lowercase(Locale.ROOT).contains(q) ||
                movie.director.lowercase(Locale.ROOT).contains(q) ||
                movie.genres.any { it.lowercase(Locale.ROOT).contains(q) } ||
                movie.cast.any { it.name.lowercase(Locale.ROOT).contains(q) || it.character.lowercase(Locale.ROOT).contains(q) } ||
                movie.releaseYear.toString().contains(q) ||
                movie.overview.lowercase(Locale.ROOT).contains(q)
            }

            val matchesGenre = if (genreFilter.isNullOrEmpty() || genreFilter == "All") true else {
                movie.genres.any { it.equals(genreFilter, ignoreCase = true) }
            }

            val matchesYear = if (yearFilter == null || yearFilter == 0) true else {
                movie.releaseYear == yearFilter
            }

            val matchesRating = if (minRating == null || minRating == 0.0) true else {
                movie.rating >= minRating
            }

            matchesQuery && matchesGenre && matchesYear && matchesRating
        }

        results = when (sortBy) {
            "Rating" -> results.sortedByDescending { it.rating }
            "Newest" -> results.sortedByDescending { it.releaseYear }
            "Oldest" -> results.sortedBy { it.releaseYear }
            "A-Z" -> results.sortedBy { it.title }
            else -> results.sortedByDescending { it.voteCount } // Popularity
        }

        return results
    }

    // Watchlist
    fun getAllWatchlist(): Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    fun isInWatchlist(movieId: String): Flow<Boolean> = watchlistDao.isInWatchlist(movieId)

    suspend fun toggleWatchlist(movie: Movie): Boolean {
        val existing = watchlistDao.getWatchlistById(movie.id)
        return if (existing != null) {
            watchlistDao.deleteById(movie.id)
            false
        } else {
            watchlistDao.insert(
                WatchlistEntity(
                    movieId = movie.id,
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    backdropUrl = movie.backdropUrl,
                    releaseYear = movie.releaseYear,
                    rating = movie.rating,
                    genres = movie.genres.joinToString(", "),
                    runtimeMinutes = movie.runtimeMinutes,
                    resolutionBadge = movie.resolutionBadge
                )
            )
            true
        }
    }

    suspend fun removeFromWatchlist(movieId: String) {
        watchlistDao.deleteById(movieId)
    }

    suspend fun markWatched(movieId: String, isWatched: Boolean) {
        watchlistDao.updateWatchedStatus(movieId, isWatched)
    }

    // Watch History & Continue Watching
    fun getAllHistory(): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getAllHistory()

    fun getContinueWatching(): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getContinueWatching()

    suspend fun getHistoryItem(movieId: String): WatchHistoryEntity? = watchHistoryDao.getHistoryByMovieId(movieId)

    suspend fun savePlaybackProgress(movie: Movie, positionMs: Long, durationMs: Long, streamUrl: String) {
        if (durationMs <= 0) return
        val percentage = ((positionMs.toDouble() / durationMs.toDouble()) * 100).toInt().coerceIn(0, 100)
        watchHistoryDao.insertOrUpdate(
            WatchHistoryEntity(
                movieId = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl,
                backdropUrl = movie.backdropUrl,
                videoUrl = streamUrl,
                lastPositionMs = positionMs,
                totalDurationMs = durationMs,
                watchedPercentage = percentage,
                lastWatchedTime = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeFromHistory(movieId: String) {
        watchHistoryDao.deleteByMovieId(movieId)
    }

    suspend fun clearHistory() {
        watchHistoryDao.clearAll()
    }

    // Search History
    fun getSearchHistory(): Flow<List<String>> = searchHistoryDao.getRecentSearches()

    suspend fun saveSearchQuery(query: String) {
        if (query.isNotBlank()) {
            searchHistoryDao.insert(SearchHistoryEntity(query = query.trim()))
        }
    }

    suspend fun deleteSearchQuery(query: String) {
        searchHistoryDao.deleteQuery(query)
    }

    suspend fun clearSearchHistory() {
        searchHistoryDao.clearHistory()
    }

    // Downloads (Legal Open Sources)
    fun getAllDownloads(): Flow<List<DownloadEntity>> = downloadDao.getAllDownloads()

    suspend fun addDownload(movie: Movie, source: com.example.data.model.VideoResolutionSource, localPath: String) {
        downloadDao.insertOrUpdate(
            DownloadEntity(
                movieId = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl,
                downloadUrl = source.url,
                localFilePath = localPath,
                resolution = source.label,
                totalBytes = source.bitrate * movie.runtimeMinutes * 60 / 8,
                downloadedBytes = 0,
                progress = 0,
                status = com.example.data.local.DownloadStatus.QUEUED.name
            )
        )
    }

    suspend fun updateDownloadStatus(movieId: String, status: com.example.data.local.DownloadStatus) {
        downloadDao.updateStatus(movieId, status.name)
    }

    suspend fun removeDownload(movieId: String) {
        downloadDao.deleteDownload(movieId)
    }
}
