package com.michambita.feature.inventario.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.core.domain.model.Producto

import com.michambita.core.common.UiState
import com.michambita.core.common.Screen
import com.michambita.feature.inventario.components.InventarioContent
import com.michambita.core.ui.components.widget.LoadingOverlay
import com.michambita.feature.inventario.viewmodel.InventarioViewModel

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    navController: NavController,
    viewModel: InventarioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateGetAllProducto.collectAsStateWithLifecycle()

    var productoList by remember { mutableStateOf(mutableListOf<Producto>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllProducto()
    }

    InventarioContent(
        productos = productoList,
        modifier = Modifier.fillMaxSize(),
        onAddProduct = { navController.navigate(Screen.Producto.route) },
        onChangeStock = { id, stock ->
            viewModel.updateStock(id, stock)
        },
        onOpenEditProduct = { p ->
            val id = p.id
            if (!id.isNullOrBlank()) {
                navController.navigate("${Screen.Producto.route}/$id")
            }
        }
    )

    when (uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            LoadingOverlay(modifier = Modifier, message = "Cargando...")
        }
        is UiState.Success -> {
            productoList = (uiState as UiState.Success<List<Producto>>).data as MutableList<Producto>
        }
        is UiState.Error -> {}
    }
}
