package com.michambita.ui.screen.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.domain.model.Producto
import com.michambita.ui.common.UiState
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.ui.viewmodel.InventoryViewModel
import com.michambita.utils.DismissKeyboardWrapper

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateGetAllProducto.collectAsStateWithLifecycle()

    var productoList by remember { mutableStateOf(mutableListOf<Producto>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllProducto()
    }

    InventoryTable(
        productos = productoList,
        onEdit = { producto ->
            println("Editar: $producto")
        },
        onDelete = { producto ->
            productoList.remove(producto)
        },
        onSave = {
            println("Guardar inventario")
        },
        onStockClick = {
            println("Actualizar stock")
        }
    )

    when (uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            LoadingOverlay(modifier = Modifier, message = "Cargando...")
        }
        is UiState.Success -> {
            productoList =
                (uiState as UiState.Success<List<Producto>>).data as MutableList<Producto>
        }
        is UiState.Error -> {}
    }
}