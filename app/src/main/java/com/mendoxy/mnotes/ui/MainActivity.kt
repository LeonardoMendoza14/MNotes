package com.mendoxy.mnotes.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mendoxy.mnotes.navigation.AppNavigation
import com.mendoxy.mnotes.navigation.AppRoutes
import com.mendoxy.mnotes.ui.presentation.SettingsViewModel
import com.mendoxy.mnotes.ui.presentation.login.LoginScreen
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.HomeScreen
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    lateinit var navController: NavHostController
    private val vm : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by vm.settingsState.collectAsState()
            navController = rememberNavController()
            MNotesTheme(settings = state) {
                Log.e("DEBUG", "Entro a MNotesTheme")
                AppNavigation(navController)
            }
        }
    }
}