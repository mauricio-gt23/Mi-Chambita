package com.michambita.router

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.michambita.common.Screen
import com.michambita.feature.home.screen.HomeScreen
import com.michambita.feature.item.screen.ItemScreen
import com.michambita.feature.inventario.screen.InventarioScreen
import com.michambita.feature.profile.screen.ProfileScreen
import com.michambita.feature.history.screen.HistoryScreen
import com.michambita.common.UiState
import com.michambita.domain.enums.BusinessType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    businessType: BusinessType,
    onLogout: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiStateGetUser.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    // Observar la ruta actual en el backstack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val welcomeTitle = when (val state = uiState) {
        is UiState.Success -> {
            val user = state.data
            if (!user.name.isNullOrBlank()) {
                "¡Bienvenido de nuevo, ${user.name} 👋!"
            } else {
                "¡Bienvenido!"
            }
        }
        is UiState.Loading -> "Cargando perfil..."
        else -> "¡Bienvenido!"
    }

    // Configurar título dinámico, botón de navegación y acciones según la ruta
    val topBarTitle = when (currentRoute) {
        Screen.HomeScreen.route -> welcomeTitle
        Screen.Profile.route -> "Mi Perfil"
        Screen.Inventario.route -> "Inventario"
        Screen.Item.route -> "Registrar Producto"
        "${Screen.Item.route}/{id}" -> "Editar Producto"
        Screen.History.route -> "Historial"
        else -> "Mi Chambita"
    }
    val navigationIcon: @Composable (() -> Unit)? = if (currentRoute != Screen.HomeScreen.route) {
        {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        }
    } else {
        null
    }
    val actions: @Composable (() -> Unit) = {
        if (currentRoute == Screen.HomeScreen.route) {
            IconButton(onClick = {
                if (navController.currentDestination?.route != Screen.Profile.route) {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                }
            }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                navigationIcon = navigationIcon ?: {},
                actions = { actions() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(
                    businessType = businessType,
                    onProductosClick = {
                        if (navController.currentDestination?.route != Screen.Item.route) {
                            navController.navigate(Screen.Item.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onInventarioClick = {
                        if (navController.currentDestination?.route != Screen.Inventario.route) {
                            navController.navigate(Screen.Inventario.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onHistorialClick = {
                        if (navController.currentDestination?.route != Screen.History.route) {
                            navController.navigate(Screen.History.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Item.route) {
                ItemScreen(
                    businessType = businessType,
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable("${Screen.Item.route}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                ItemScreen(
                    businessType = businessType,
                    itemId = id,
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable(Screen.Inventario.route) {
                InventarioScreen(
                    onAddItem = {
                        if (navController.currentDestination?.route != Screen.Item.route) {
                            navController.navigate(Screen.Item.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onOpenEditItem = { item ->
                        val id = item.id
                        if (!id.isNullOrBlank()) {
                            val editRoute = "${Screen.Item.route}/$id"
                            if (navController.currentDestination?.route != editRoute) {
                                navController.navigate(editRoute) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = onLogout,
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(businessType = businessType)
            }
        }
    }
}