package com.mendoxy.mnotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mendoxy.mnotes.ui.presentation.login.LoginScreen
import com.mendoxy.mnotes.ui.presentation.login.register.RegisterScreen
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.HomeScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Home.route
    ) {
        composable(route = AppRoutes.Login.route){
            LoginScreen()
        }

        composable(route = AppRoutes.Register.route){
            RegisterScreen()
        }

        composable(route = AppRoutes.Home.route){
            HomeScreen()
        }

    }
}