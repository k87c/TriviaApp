package com.example.triviaapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triviaapp.ui.screens.GameScreen
import com.example.triviaapp.ui.screens.HomeScreen

enum class Screen(val route: String) {
    Home("home"),
    Game("game")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame =
                { quantity, record ->
                    navController.navigate("${Screen.Game.route}/$quantity/$record")
                }
            )
        }
        composable(Screen.Home.route + "/{record}") {
            val record = it.arguments?.getInt("record") ?: 0
            HomeScreen(
                record = record,
                onStartGame =
                { quantity, record ->
                    navController.navigate("${Screen.Game.route}/$quantity/$record")
                }
            )
        }
        composable(Screen.Game.route + "/{quantity}/{record}") { backStackEntry ->
            val quantity = backStackEntry.arguments?.getString("quantity")?.toIntOrNull() ?: 0
            val record = backStackEntry.arguments?.getString("record")?.toIntOrNull() ?: 0
            GameScreen(
                quantity = quantity, record = record,
                onBackToHome = {
                    navController.navigate(Screen.Home.route + "/$it")
                }
            )
        }
    }
}