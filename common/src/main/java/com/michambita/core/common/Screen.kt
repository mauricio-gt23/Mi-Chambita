package com.michambita.common


sealed class Screen(val route: String) {
    object MainContainer: Screen("main_container")

    object Splash : Screen("splash")
    object Login : Screen("login")
    object Registro : Screen("registro")
    object HomeScreen: Screen("home")
    object Item : Screen("item")
    object Inventario : Screen("inventario")
    object Profile : Screen("profile")
    object History : Screen("history")

//    object Ventas : Screen("ventas")
//    object Gastos : Screen("gastos")
//    object Inventario : Screen("inventario")
//    object Resumen : Screen("resumen")
    // object DetalleVenta : Screen("ventas/{ventaId}") // para rutas con parámetros
}