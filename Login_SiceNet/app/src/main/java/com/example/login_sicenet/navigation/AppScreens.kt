package com.example.login_sicenet.navigation

sealed class AppScreens(val route : String) {
    object LoginScreen : AppScreens("login_screen")
    object DataScreen : AppScreens("data_screen")

    object AboutScreen : AppScreens("about_screen")
}