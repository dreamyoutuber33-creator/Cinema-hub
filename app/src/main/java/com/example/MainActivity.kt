package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.CinemaDatabase
import com.example.data.local.UserPreferencesRepository
import com.example.data.repository.MovieRepository
import com.example.data.repository.NetworkMonitor
import com.example.ui.components.CinematicBottomBar
import com.example.ui.components.CustomStreamDialog
import com.example.ui.navigation.Screen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MovieDetailsScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HistoryViewModel
import com.example.ui.viewmodel.HomeViewModel
import com.example.ui.viewmodel.MovieDetailsViewModel
import com.example.ui.viewmodel.PlayerViewModel
import com.example.ui.viewmodel.SearchViewModel
import com.example.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private var isPipMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = CinemaDatabase.getInstance(applicationContext)
        val repository = MovieRepository(database)
        val preferencesRepository = UserPreferencesRepository(applicationContext)
        val networkMonitor = NetworkMonitor(applicationContext)

        setContent {
            MyApplicationTheme {
                CinemaHubApp(
                    repository = repository,
                    preferencesRepository = preferencesRepository,
                    networkMonitor = networkMonitor,
                    isPipMode = isPipMode
                )
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPipMode = isInPictureInPictureMode
    }
}

@Composable
fun CinemaHubApp(
    repository: MovieRepository,
    preferencesRepository: UserPreferencesRepository,
    networkMonitor: NetworkMonitor,
    isPipMode: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showCustomStreamDialog by remember { mutableStateOf(false) }

    // Identify if current destination is a full-screen player or PiP
    val isPlayerScreen = currentRoute?.startsWith("player") == true
    val showBottomBar = !isPlayerScreen && !isPipMode

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (showBottomBar) {
                CinematicBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = AmoledBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AmoledBlack)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                // Home Screen
                composable(Screen.Home.route) {
                    val homeViewModel = remember {
                        HomeViewModel(repository, networkMonitor)
                    }
                    HomeScreen(
                        viewModel = homeViewModel,
                        onMovieClick = { movie ->
                            navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                        },
                        onPlayMovie = { movie ->
                            navController.navigate(Screen.Player.createRoute(movie.id))
                        },
                        onSearchClick = {
                            navController.navigate(Screen.Search.route)
                        },
                        onCustomStreamClick = {
                            showCustomStreamDialog = true
                        }
                    )
                }

                // Search Screen
                composable(Screen.Search.route) {
                    val searchViewModel = remember {
                        SearchViewModel(repository)
                    }
                    SearchScreen(
                        viewModel = searchViewModel,
                        onMovieClick = { movie ->
                            navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                        }
                    )
                }

                // Watchlist Screen
                composable(Screen.Watchlist.route) {
                    val watchlistViewModel = remember {
                        com.example.ui.viewmodel.WatchlistViewModel(repository)
                    }
                    com.example.ui.screens.WatchlistScreen(
                        viewModel = watchlistViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate(Screen.MovieDetail.createRoute(movieId))
                        },
                        onPlayMovie = { movieId ->
                            navController.navigate(Screen.Player.createRoute(movieId))
                        },
                        onExploreClick = {
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }

                // History Screen
                composable(Screen.History.route) {
                    val historyViewModel = remember {
                        HistoryViewModel(repository)
                    }
                    HistoryScreen(
                        viewModel = historyViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate(Screen.MovieDetail.createRoute(movieId))
                        },
                        onPlayMovie = { movieId ->
                            navController.navigate(Screen.Player.createRoute(movieId))
                        },
                        onExploreClick = {
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }

                // Settings Screen
                composable(Screen.Settings.route) {
                    val settingsViewModel = remember {
                        SettingsViewModel(
                            context = navController.context,
                            preferencesRepository = preferencesRepository,
                            movieRepository = repository
                        )
                    }
                    SettingsScreen(viewModel = settingsViewModel)
                }

                // Movie Details Screen
                composable(
                    route = Screen.MovieDetail.route,
                    arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                    val detailsViewModel = remember(movieId) {
                        MovieDetailsViewModel(movieId, repository)
                    }
                    MovieDetailsScreen(
                        viewModel = detailsViewModel,
                        onBackClick = { navController.popBackStack() },
                        onPlayMovie = { movie ->
                            navController.navigate(Screen.Player.createRoute(movie.id))
                        },
                        onMovieClick = { movie ->
                            navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                        }
                    )
                }

                // Video Player Screen
                composable(
                    route = Screen.Player.route,
                    arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                    val movie = repository.getMovieById(movieId)
                    val title = movie?.title ?: "Video Player"

                    val playerViewModel = remember(movieId) {
                        PlayerViewModel(
                            context = navController.context,
                            movieRepository = repository,
                            preferencesRepository = preferencesRepository
                        )
                    }

                    PlayerScreen(
                        movieId = movieId,
                        viewModel = playerViewModel,
                        movieTitle = title,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Custom Stream Dialog
            if (showCustomStreamDialog) {
                CustomStreamDialog(
                    onDismiss = { showCustomStreamDialog = false },
                    onPlayUrl = { url, title ->
                        showCustomStreamDialog = false
                        // For custom stream, we can play using the player directly or via a preset id
                        val matchedMovie = repository.getAllMovies().find { m -> m.videoSources.any { it.url == url } || m.trailerUrl == url }
                        if (matchedMovie != null) {
                            navController.navigate(Screen.Player.createRoute(matchedMovie.id))
                        } else {
                            // Default to first movie player and custom play
                            val fallback = repository.getAllMovies().first()
                            navController.navigate(Screen.Player.createRoute(fallback.id))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = "Welcome to CinemaHub, $name!",
        color = com.example.ui.theme.CinemaGold,
        modifier = modifier
    )
}
