package com.example.login_sicenet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login_sicenet.screens.DataScreen
import com.example.login_sicenet.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController) }
        composable("data") { DataScreen(navController = navController) }
    }
}