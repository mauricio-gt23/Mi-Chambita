package com.michambita.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michambita.navigation.Screen
import com.michambita.ui.screen.HomeScreen
import com.michambita.ui.screen.ProductoScreen
import com.michambita.ui.viewmodel.MainViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.screen.InventarioScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiStateGetUser.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    val topBarTitle = when (val state = uiState) {
        is UiState.Success -> {
            val user = state.data
            if (user.name!!.isNotBlank()) {
                "¡Bienvenido de nuevo, ${user.name} 👋!"
            } else {
                "¡Bienvenido!"
            }
        }
        is UiState.Loading -> "Cargando perfil..."
        else -> "¡Bienvenido!"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                actions = {
                    IconButton(onClick = { /* Acción perfil o logout */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainContainer.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.MainContainer.route) { HomeScreen(navController) }
            composable(Screen.Producto.route) { ProductoScreen() }
            composable(Screen.Inventario.route) { InventarioScreen(navController) }
//            composable(Screen.Gastos.route) { GastosScreen(navController) }
//            composable(Screen.Resumen.route) { ResumenScreen(navController) }
        }
    }
}