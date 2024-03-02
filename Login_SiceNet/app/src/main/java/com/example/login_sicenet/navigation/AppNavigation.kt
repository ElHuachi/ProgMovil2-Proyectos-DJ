package com.example.login_sicenet.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.screens.AboutScreen
import com.example.login_sicenet.screens.CalFinalScreen
import com.example.login_sicenet.screens.CalParScreen
import com.example.login_sicenet.screens.DataScreen
import com.example.login_sicenet.screens.DataViewModel
import com.example.login_sicenet.screens.HorarioScreen
import com.example.login_sicenet.screens.LoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val dataViewModel: DataViewModel =
        viewModel(factory = DataViewModel.Factory)
    NavHost(navController, startDestination = "login") {//Cambiar solo para pruebas en pantallas diferentes
        composable("login") { LoginScreen(navController = navController, viewModel= dataViewModel) }
        composable("data") { DataScreen(navController = navController, viewModel= dataViewModel) }
        composable("about_screen") { AboutScreen(navController = navController)}
        composable("horario_screen") { HorarioScreen(navController = navController, viewModel = dataViewModel)}
        composable("final_screen") { CalFinalScreen (navController = navController, viewModel = dataViewModel)}
        composable("calpar_screen") { CalParScreen(navController = navController, viewModel = dataViewModel)}
    }
}