package com.fixpix.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val iconFilled: ImageVector? = null, val iconOutlined: ImageVector? = null) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Projects : Screen("projects", "Projects", Icons.Filled.GridView, Icons.Outlined.GridView)
    object Editor : Screen("editor/{projectId}", "Editor", Icons.Filled.AutoFixHigh, Icons.Outlined.AutoFixHigh) {
        fun createRoute(projectId: String) = "editor/$projectId"
    }
    object Settings : Screen("settings", "Settings", Icons.Filled.Person, Icons.Outlined.Person)
    
    // Bottom Nav Items
    companion object {
        val bottomNavItems = listOf(Dashboard, Projects, Settings)
    }
}
