package com.mendoxy.mnotes.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.mendoxy.mnotes.ui.presentation.login.LoginScreen
import com.mendoxy.mnotes.ui.presentation.login.register.RegisterScreen
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.HomeScreen

@Composable
fun AppNavigation(navController: NavHostController){
    val startDestination by remember {mutableStateOf(if(FirebaseAuth.getInstance().currentUser != null){
        AppRoutes.Home.route
    }else{
        AppRoutes.Login.route
    }
    )}

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {fadeIn()},
        exitTransition = { fadeOut() }
    ) {

        composable(route = AppRoutes.Login.route){
            LoginScreen(navController)
        }

        composable(route = AppRoutes.Register.route){
            RegisterScreen(navController)
        }

        composable(route = AppRoutes.Home.route){
            HomeScreen(navController)
        }

    }
}