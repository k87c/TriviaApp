package com.example.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.triviaapp.ui.navigation.AppNavigation
import com.example.triviaapp.ui.theme.TriviaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaAppTheme {
                AppNavigation()
            }
        }
    }
}