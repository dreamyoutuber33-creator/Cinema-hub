package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Watchlist : Screen("watchlist")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Downloads : Screen("downloads")

    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String): String = "movie_detail/$movieId"
    }

    object Player : Screen("player/{movieId}") {
        fun createRoute(movieId: String): String = "player/$movieId"
    }
}
