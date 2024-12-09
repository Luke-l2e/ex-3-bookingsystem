package com.example.aufgabe3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.aufgabe3.navigation.AppNavHost
import com.example.aufgabe3.ui.theme.Aufgabe3Theme

/**
 * The main activity of the application.
 *
 * This activity serves as the entry point of the app and sets up the UI theme, navigation host,
 * and the main content. It uses Jetpack Compose to define the UI components and applies the
 * application's theme.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aufgabe3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

