package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WatchlistEntity
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WatchlistUiState(
    val searchQuery: String = "",
    val filterWatched: Boolean? = null, // null = all, true = watched, false = unwatched
    val sortBy: String = "Date Added" // Date Added, Rating, Title
)

class WatchlistViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    val watchlistItems: StateFlow<List<WatchlistEntity>> = combine(
        repository.getAllWatchlist(),
        _uiState
    ) { allItems, state ->
        var list = allItems.filter { item ->
            val matchesQuery = state.searchQuery.isBlank() ||
                    item.title.contains(state.searchQuery, ignoreCase = true) ||
                    item.genres.contains(state.searchQuery, ignoreCase = true)
            val matchesFilter = state.filterWatched == null || item.isWatched == state.filterWatched
            matchesQuery && matchesFilter
        }

        list = when (state.sortBy) {
            "Rating" -> list.sortedByDescending { it.rating }
            "Title" -> list.sortedBy { it.title }
            else -> list.sortedByDescending { it.addedAt }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setFilterWatched(filter: Boolean?) {
        _uiState.update { it.copy(filterWatched = filter) }
    }

    fun setSortBy(sort: String) {
        _uiState.update { it.copy(sortBy = sort) }
    }

    fun removeFromWatchlist(movieId: String) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movieId)
        }
    }

    fun toggleWatched(movieId: String, currentlyWatched: Boolean) {
        viewModelScope.launch {
            repository.markWatched(movieId, !currentlyWatched)
        }
    }
}
