package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Movie
import com.example.ui.components.CinematicTopBar
import com.example.ui.components.ContinueWatchingSection
import com.example.ui.components.HeroBanner
import com.example.ui.components.MovieCard
import com.example.ui.components.MovieSection
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (Movie) -> Unit,
    onPlayMovie: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onCustomStreamClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val continueWatchingList by viewModel.continueWatching.collectAsState()
    val watchlistIds by viewModel.watchlistMovieIds.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // App Top Bar
        CinematicTopBar(
            isOffline = state.isOffline,
            onSearchClick = onSearchClick,
            onCustomStreamClick = onCustomStreamClick
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CinemaGold)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("home_movie_list"),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Hero Banner
                state.featuredMovie?.let { hero ->
                    item(key = "hero_banner") {
                        HeroBanner(
                            movie = hero,
                            isInWatchlist = watchlistIds.contains(hero.id),
                            onPlayClick = { onPlayMovie(hero) },
                            onWatchlistClick = { viewModel.toggleWatchlist(hero) },
                            onDetailsClick = { onMovieClick(hero) }
                        )
                    }
                }

                // Genre Filter Chips
                item(key = "genre_chips") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.availableGenres) { genre ->
                            val isSelected = state.selectedGenre == genre
                            val bgColor = if (isSelected) CinemaGold else SurfaceCard
                            val textColor = if (isSelected) AmoledBlack else TextSecondary
                            val borderColor = if (isSelected) CinemaGold else SurfaceBorder

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                                    .clickable { viewModel.selectGenre(genre) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                                    .testTag("genre_chip_$genre"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = genre,
                                    color = textColor,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // If specific genre selected (not "All"), show genre-specific section prominently
                if (state.selectedGenre != "All") {
                    item(key = "selected_genre_section") {
                        MovieSection(
                            title = "${state.selectedGenre} Movies",
                            movies = state.genreMovies,
                            onMovieClick = onMovieClick
                        )
                    }
                }

                // Continue Watching (if unfinished videos exist)
                if (continueWatchingList.isNotEmpty()) {
                    item(key = "continue_watching") {
                        ContinueWatchingSection(
                            historyList = continueWatchingList,
                            onResumeClick = { historyItem ->
                                val movie = viewModel.uiState.value.trendingMovies.find { it.id == historyItem.movieId }
                                    ?: viewModel.uiState.value.popularMovies.find { it.id == historyItem.movieId }
                                    ?: state.featuredMovie
                                if (movie != null) {
                                    onPlayMovie(movie)
                                }
                            }
                        )
                    }
                }

                // Trending Movies
                item(key = "trending_movies") {
                    MovieSection(
                        title = "Trending Now",
                        movies = state.trendingMovies,
                        onMovieClick = onMovieClick
                    )
                }

                // Popular Movies
                item(key = "popular_movies") {
                    MovieSection(
                        title = "Popular on CinemaHub",
                        movies = state.popularMovies,
                        onMovieClick = onMovieClick
                    )
                }

                // Top Rated Movies
                item(key = "top_rated_movies") {
                    MovieSection(
                        title = "Top Rated Masterpieces",
                        movies = state.topRatedMovies,
                        onMovieClick = onMovieClick
                    )
                }

                // Now Playing / Modern Releases
                item(key = "now_playing_movies") {
                    MovieSection(
                        title = "Recent Open Films",
                        movies = state.nowPlayingMovies,
                        onMovieClick = onMovieClick
                    )
                }

                // Public Domain Classics
                item(key = "classics_section") {
                    val classics = state.trendingMovies.filter { it.genres.contains("Classics") }
                    if (classics.isNotEmpty()) {
                        MovieSection(
                            title = "Public Domain Classics",
                            movies = classics,
                            onMovieClick = onMovieClick
                        )
                    }
                }

                // Recommended For You
                item(key = "recommended_movies") {
                    MovieSection(
                        title = "Recommended For You",
                        movies = state.recommendedMovies,
                        onMovieClick = onMovieClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
