package com.devx.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.devx.anime.navigation.AnimeNavHost
import com.devx.anime.ui.theme.AnimeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AnimeAppTheme {
                val navController = rememberNavController()
                AnimeNavHost(navController = navController)
            }
        }
    }
}
