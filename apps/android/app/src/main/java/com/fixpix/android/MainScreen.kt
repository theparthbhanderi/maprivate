package com.fixpix.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.components.navigation.FixPixBottomNav
import com.fixpix.android.ui.screens.DashboardScreen
import com.fixpix.android.ui.screens.LoginScreen
import com.fixpix.android.ui.screens.RegisterScreen
import com.fixpix.android.ui.screens.ProjectsScreen
import com.fixpix.android.ui.screens.SettingsScreen
import com.fixpix.android.ui.screens.SplashScreen
import com.fixpix.android.ui.editor.EditorScreen
import com.fixpix.android.ui.theme.FixPixTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    FixPixTheme {
        Scaffold(
            bottomBar = { FixPixBottomNav(navController = navController) },
            containerColor = FixPixTheme.colors.background
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()) // We handle top padding inside screens for Large Title
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(navController = navController)
                }
                composable(Screen.Login.route) {
                    LoginScreen(navController = navController)
                }
                composable(Screen.Register.route) {
                    RegisterScreen(navController = navController)
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen(navController = navController)
                }
                composable(Screen.Projects.route) {
                    ProjectsScreen(navController = navController)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
                composable(Screen.Editor.route) { backStackEntry ->
                    val projectId = backStackEntry.arguments?.getString("projectId") ?: "new"
                    EditorScreen(projectId = projectId, navController = navController)
                }
            }
        }
    }
}
