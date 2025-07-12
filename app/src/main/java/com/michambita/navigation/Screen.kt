package com.michambita.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")

    object Main : Screen("may")

    object Ventas : Screen("ventas")
    object Gastos : Screen("gastos")

    object Inventario : Screen("inventario")

    object Resumen : Screen("resumen")

    // object DetalleVenta : Screen("ventas/{ventaId}") // para rutas con parámetros
}