package com.michambita.ui

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michambita.navigation.Screen
import com.michambita.ui.screen.HomeScreen
import com.michambita.ui.screen.producto.ProductoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¡Bienvenido de nuevo, M 👋!") },
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
            modifier = Modifier.padding(PaddingValues(
                start = 0.dp, top = innerPadding.calculateTopPadding(),
                end = 0.dp, bottom = 0.dp
            ))
        ) {
            composable(Screen.MainContainer.route) { HomeScreen(navController) }
            composable(Screen.Producto.route) { ProductoScreen() }
//            composable(Screen.Gastos.route) { GastosScreen(navController) }
//            composable(Screen.Inventario.route) { InventarioScreen(navController) }
//            composable(Screen.Resumen.route) { ResumenScreen(navController) }
        }
    }
}