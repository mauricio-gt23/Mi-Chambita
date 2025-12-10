package com.michambita.navigation

sealed class Screen(val route: String) {
    object MainContainer: Screen("main_container")

    object Splash : Screen("splash")
    object Login : Screen("login")
    object Registro : Screen("registro")
    object HomeScreen: Screen("home")
    object Producto : Screen("producto")
    object Inventario : Screen("inventario")

//    object Ventas : Screen("ventas")
//    object Gastos : Screen("gastos")
//    object Inventario : Screen("inventario")
//    object Resumen : Screen("resumen")
    // object DetalleVenta : Screen("ventas/{ventaId}") // para rutas con parámetros
}