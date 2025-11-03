package com.mendoxy.mnotes.navigation

sealed class AppRoutes(val route: String){
    data object Login: AppRoutes("login_screen")
    data object Register: AppRoutes("register_screen")
    data object Home: AppRoutes("home_screen")
}
