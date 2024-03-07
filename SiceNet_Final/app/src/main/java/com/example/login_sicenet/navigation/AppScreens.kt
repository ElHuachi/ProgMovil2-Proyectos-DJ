package com.example.login_sicenet.navigation

sealed class AppScreens(val route : String) {
    object LoginScreen : AppScreens("login_screen")
    object DataScreen : AppScreens("data_screen")
    object AboutScreen : AppScreens("about_screen")
    object CalFinalScreen : AppScreens("final_screen")
    object HorarioScreen : AppScreens("horario_screen")
    object CalParScreen : AppScreens("calpar_screen")
}