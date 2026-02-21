package com.fixpix.android.ui.components.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun FixPixBottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Don't show bottom bar on Splash, Login, or Editor screen
    if (currentRoute == Screen.Splash.route || 
        currentRoute == Screen.Login.route ||
        currentRoute?.startsWith("editor") == true) return

    NavigationBar(
        containerColor = FixPixTheme.colors.surface.copy(alpha = 0.9f),
        contentColor = FixPixTheme.colors.primary,
        tonalElevation = 8.dp,
        modifier = Modifier.height(88.dp)
    ) {
        Screen.bottomNavItems.forEach { screen ->
            val selected = currentRoute == screen.route
            
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = if (selected) screen.iconFilled!! else screen.iconOutlined!!,
                        contentDescription = screen.title,
                        modifier = Modifier.size(26.dp)
                    ) 
                },
                label = { 
                    Text(
                        text = screen.title!!,
                        style = FixPixTheme.typography.caption1
                    ) 
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FixPixTheme.colors.primary,
                    selectedTextColor = FixPixTheme.colors.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = FixPixTheme.colors.textSecondary,
                    unselectedTextColor = FixPixTheme.colors.textSecondary
                )
            )
        }
    }
}
