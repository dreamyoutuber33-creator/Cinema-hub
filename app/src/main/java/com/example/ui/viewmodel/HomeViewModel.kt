package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WatchHistoryEntity
import com.example.data.model.Movie
import com.example.data.repository.LegalMoviesDataSource
import com.example.data.repository.MovieRepository
import com.example.data.repository.NetworkMonitor
import com.example.data.repository.NetworkType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val featuredMovie: Movie? = null,
    val trendingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val recommendedMovies: List<Movie> = emptyList(),
    val selectedGenre: String = "All",
    val genreMovies: List<Movie> = emptyList(),
    val availableGenres: List<String> = LegalMoviesDataSource.genres,
    val isLoading: Boolean = false,
    val isOffline: Boolean = false
)

class HomeViewModel(
    private val repository: MovieRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val continueWatching: StateFlow<List<WatchHistoryEntity>> = repository.getContinueWatching()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val watchlistMovieIds: StateFlow<Set<String>> = repository.getAllWatchlist()
        .map { list -> list.map { it.movieId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        loadHomeData()
        monitorNetwork()
    }

    private fun loadHomeData() {
        val all = repository.getAllMovies()
        val featured = all.firstOrNull { it.isFeatured } ?: all.firstOrNull()
        val trending = repository.getTrendingMovies()
        val popular = repository.getPopularMovies()
        val nowPlaying = repository.getNowPlayingMovies()
        val topRated = repository.getTopRatedMovies()
        val upcoming = repository.getUpcomingMovies()
        val recommended = repository.getRecommendedMovies(featured?.id)

        _uiState.update {
            it.copy(
                featuredMovie = featured,
                trendingMovies = trending,
                popularMovies = popular,
                nowPlayingMovies = nowPlaying,
                topRatedMovies = topRated,
                upcomingMovies = upcoming,
                recommendedMovies = recommended,
                genreMovies = all,
                isLoading = false
            )
        }
    }

    private fun monitorNetwork() {
        viewModelScope.launch {
            networkMonitor.networkTypeFlow.collect { netType ->
                _uiState.update { it.copy(isOffline = netType == NetworkType.OFFLINE) }
            }
        }
    }

    fun selectGenre(genre: String) {
        val movies = repository.getMoviesByGenre(genre)
        _uiState.update {
            it.copy(
                selectedGenre = genre,
                genreMovies = movies
            )
        }
    }

    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            repository.toggleWatchlist(movie)
        }
    }
}
