package com.mendoxy.mnotes.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
        Log.e("DEBUG", "Entro a HomeDefault")
        AppRoutes.Home.route
    }else{
        Log.e("DEBUG", "Entro a LoginDefault")
        AppRoutes.Login.route
    }
    )}

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn()
        }
    ) {
        composable(
            route = AppRoutes.Login.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    tween(1000)
                )
            }
        ) { LoginScreen(navController) }

        composable(
            AppRoutes.Register.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(1000)
                )
            }
        ) { RegisterScreen(navController) }

        composable(
            AppRoutes.Home.route,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(1000)
                )
            }
        ) { HomeScreen(navController) }
    }


}