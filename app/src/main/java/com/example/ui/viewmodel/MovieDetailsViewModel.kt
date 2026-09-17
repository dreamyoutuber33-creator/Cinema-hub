package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetailsUiState(
    val movie: Movie? = null,
    val similarMovies: List<Movie> = emptyList(),
    val isInWatchlist: Boolean = false,
    val isWatched: Boolean = false
)

class MovieDetailsViewModel(
    private val movieId: String,
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
        observeWatchlist()
    }

    private fun loadMovie() {
        val movie = repository.getMovieById(movieId)
        val similar = repository.getRecommendedMovies(movieId)

        _uiState.update {
            it.copy(
                movie = movie,
                similarMovies = similar
            )
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            repository.isInWatchlist(movieId).collect { isIn ->
                _uiState.update { it.copy(isInWatchlist = isIn) }
            }
        }
    }

    fun toggleWatchlist() {
        val movie = _uiState.value.movie ?: return
        viewModelScope.launch {
            repository.toggleWatchlist(movie)
        }
    }

    fun markWatched(isWatched: Boolean) {
        viewModelScope.launch {
            repository.markWatched(movieId, isWatched)
            _uiState.update { it.copy(isWatched = isWatched) }
        }
    }
}
