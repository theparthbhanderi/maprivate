package com.fixpix.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fixpix.ui.screens.*

@Composable
fun FixPixNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splash"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen(onSplashFinished = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    // MVP: Navigate to dashboard on click (Simulated login)
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignupClick = { /* Navigate to register if implemented */ }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNewProjectClick = { navController.navigate("new_project") },
                onProjectClick = { projectId -> navController.navigate("editor/$projectId") },
                onGalleryClick = { navController.navigate("gallery") }
            )
        }

        composable("gallery") {
            GalleryScreen(
                onProjectClick = { projectId -> navController.navigate("editor/$projectId") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("new_project") {
            NewProjectScreen(
                onUploadClick = { 
                    // MVP: Just go to editor with dummy ID or handle upload
                    // Real app: Launch picker, upload, then nav
                    navController.navigate("editor/new_upload") 
                }
            )
        }

        composable("editor/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: return@composable
            
            val viewModel: com.fixpix.ui.editor.state.EditorViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val state by viewModel.uiState.collectAsState()
            
            EditorScreen(
               state = state,
               onToolSelect = viewModel::onToolSelect,
               onAdjustmentChange = viewModel::onAdjustmentChange,
               onBackClick = { navController.popBackStack() },
               onSaveClick = viewModel::onSaveClick
            )
        }
    }
}
