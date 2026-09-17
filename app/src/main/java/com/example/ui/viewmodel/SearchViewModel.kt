package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Movie
import com.example.data.repository.LegalMoviesDataSource
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val selectedGenre: String = "All",
    val selectedYear: Int? = null,
    val selectedMinRating: Double = 0.0,
    val sortBy: String = "Popularity",
    val isGridView: Boolean = true,
    val isFilterSheetOpen: Boolean = false,
    val availableGenres: List<String> = LegalMoviesDataSource.genres,
    val availableYears: List<Int> = listOf(2022, 2019, 2015, 2012, 2010, 2008, 2006, 1968, 1963, 1940, 1926),
    val sortOptions: List<String> = listOf("Popularity", "Rating", "Newest", "Oldest", "A-Z"),
    val trendingSearches: List<String> = listOf("Tears of Steel", "Sintel", "Cary Grant", "4K Sci-Fi", "Animation", "George Romero"),
    val isSearching: Boolean = false
)

class SearchViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val searchHistory: StateFlow<List<String>> = repository.getSearchHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Initially show top movies
        performSearch("")
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        performSearch(newQuery)
    }

    fun onSearchSubmitted(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                repository.saveSearchQuery(query)
            }
        }
        performSearch(query)
    }

    fun setGenreFilter(genre: String) {
        _uiState.update { it.copy(selectedGenre = genre) }
        performSearch(_uiState.value.query)
    }

    fun setYearFilter(year: Int?) {
        _uiState.update { it.copy(selectedYear = year) }
        performSearch(_uiState.value.query)
    }

    fun setMinRating(rating: Double) {
        _uiState.update { it.copy(selectedMinRating = rating) }
        performSearch(_uiState.value.query)
    }

    fun setSortBy(sort: String) {
        _uiState.update { it.copy(sortBy = sort) }
        performSearch(_uiState.value.query)
    }

    fun toggleViewMode() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }

    fun toggleFilterSheet(isOpen: Boolean) {
        _uiState.update { it.copy(isFilterSheetOpen = isOpen) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                selectedGenre = "All",
                selectedYear = null,
                selectedMinRating = 0.0,
                sortBy = "Popularity"
            )
        }
        performSearch(_uiState.value.query)
    }

    fun deleteHistoryQuery(query: String) {
        viewModelScope.launch {
            repository.deleteSearchQuery(query)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }

    private fun performSearch(query: String) {
        val state = _uiState.value
        val matches = repository.searchMovies(
            query = query,
            genreFilter = if (state.selectedGenre == "All") null else state.selectedGenre,
            yearFilter = state.selectedYear,
            minRating = if (state.selectedMinRating > 0) state.selectedMinRating else null,
            sortBy = state.sortBy
        )

        _uiState.update { it.copy(results = matches) }
    }
}
