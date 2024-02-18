package com.example.login_sicenet.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.screens.AboutScreen
import com.example.login_sicenet.screens.DataScreen
import com.example.login_sicenet.screens.DataViewModel
import com.example.login_sicenet.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val dataViewModel: DataViewModel =
        viewModel(factory = DataViewModel.Factory)
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController, viewModel= dataViewModel) }
        composable("data") { DataScreen(navController = navController, viewModel= dataViewModel) }
        composable("about_screen") { AboutScreen(navController = navController)}
    }
}