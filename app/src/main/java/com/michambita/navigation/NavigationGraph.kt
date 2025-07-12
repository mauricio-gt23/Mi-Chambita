package com.michambita.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.michambita.ui.screen.LoginScreen
import com.michambita.ui.screen.MainScreen

@Composable
fun NavigationGraph(navController: NavHostController, startDestination: String = Screen.Login.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
//        composable(Screen.Ventas.route) {
//            VentasScreen(navController)
//        }
//        composable(Screen.Gastos.route) {
//            GastosScreen(navController)
//        }
//        composable(Screen.Inventario.route) {
//            InventarioScreen(navController)
//        }
//        composable(Screen.Resumen.route) {
//            ResumenScreen(navController)
//        }

        // Ejemplo con parámetro opcional
        // composable("${Screen.Ventas.route}/{ventaId}") { backStackEntry ->
        //     val ventaId = backStackEntry.arguments?.getString("ventaId")
        //     VentasScreen(navController, ventaId)
        // }
    }
}
