package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.Screen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextSecondary

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun CinematicBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home.route, "Home", Icons.Default.Home, "nav_home"),
        NavItem(Screen.Search.route, "Search", Icons.Default.Search, "nav_search"),
        NavItem(Screen.Watchlist.route, "Watchlist", Icons.Default.Bookmark, "nav_watchlist"),
        NavItem(Screen.History.route, "History", Icons.Default.History, "nav_history"),
        NavItem(Screen.Settings.route, "Settings", Icons.Default.Settings, "nav_settings")
    )

    NavigationBar(
        containerColor = AmoledBlack,
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .border(1.dp, SurfaceBorder.copy(alpha = 0.3f)),
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CinemaGold,
                    selectedTextColor = CinemaGold,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = CinemaGold.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
