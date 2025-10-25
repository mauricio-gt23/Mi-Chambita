package com.michambita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.michambita.navigation.NavigationGraph
import com.michambita.ui.theme.MiChambitaTheme
import com.michambita.utils.DismissKeyboardWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MiChambitaTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                DismissKeyboardWrapper {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavigationGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}