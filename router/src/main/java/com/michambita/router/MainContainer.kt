package com.michambita.router

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
import com.michambita.core.common.Screen
import com.michambita.feature.home.screen.HomeScreen
import com.michambita.feature.producto.screen.ProductoScreen
import com.michambita.feature.inventario.screen.InventarioScreen
import com.michambita.router.MainViewModel
import com.michambita.core.common.UiState

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
            composable(Screen.Producto.route) { ProductoScreen(onSaveSuccess = { navController.popBackStack() }) }
            composable("${Screen.Producto.route}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                ProductoScreen(productId = id, onSaveSuccess = { navController.popBackStack() })
            }
            composable(Screen.Inventario.route) { InventarioScreen(navController) }
//            composable(Screen.Gastos.route) { GastosScreen(navController) }
//            composable(Screen.Resumen.route) { ResumenScreen(navController) }
        }
    }
}