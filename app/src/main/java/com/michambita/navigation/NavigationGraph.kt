package com.michambita.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.michambita.ui.MainContainer
import com.michambita.ui.screen.SplashScreen
import com.michambita.ui.screen.auth.AuthScreen
import com.michambita.ui.viewmodel.SessionViewModel
import com.michambita.ui.viewmodel.UserSessionState


@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val userSessionState by sessionViewModel.userSessionState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {

        composable(Screen.Splash.route) {
            SplashScreen()

            LaunchedEffect(userSessionState) {
                when (userSessionState) {
                    is UserSessionState.Authenticated -> {
                        navController.navigate(Screen.MainContainer.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                    is UserSessionState.Unauthenticated -> {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                    is UserSessionState.Unknown -> {}
                }
            }
        }

        composable(Screen.Login.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.MainContainer.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MainContainer.route) {
            MainContainer()
        }
    }
}