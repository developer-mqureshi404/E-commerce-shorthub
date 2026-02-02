package com.example.tutedude.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.tutedude.ecommerce.data.repository.AuthRepository
import com.example.tutedude.ecommerce.presentation.navigation.NavGraph
import com.example.tutedude.ecommerce.presentation.navigation.Screen
import com.example.tutedude.ecommerce.ui.theme.TutededeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutededeTheme {
                val navController = rememberNavController()
                val startDestination = if (authRepository.isUserLoggedIn()) {
                    Screen.Home.route
                } else {
                    Screen.Login.route
                }

                NavGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}