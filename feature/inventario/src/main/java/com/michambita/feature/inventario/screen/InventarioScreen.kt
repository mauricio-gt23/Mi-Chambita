package com.michambita.feature.inventario.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.common.Screen
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.feature.inventario.components.InventarioContent
import com.michambita.feature.inventario.intentmodel.InventarioIntent
import com.michambita.feature.inventario.intentmodel.InventarioIntentModel

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    navController: NavController,
    intentModel: InventarioIntentModel = hiltViewModel()
) {
    val uiState by intentModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        intentModel.sendIntent(InventarioIntent.LoadItems)
    }

    InventarioContent(
        items = uiState.items,
        modifier = Modifier.fillMaxSize(),
        onAddItem = { navController.navigate(Screen.Item.route) },
        onChangeStock = { id, stock ->
            intentModel.sendIntent(InventarioIntent.UpdateStock(id, stock))
        },
        onOpenEditItem = { item ->
            val id = item.id
            if (!id.isNullOrBlank()) {
                navController.navigate("${Screen.Item.route}/$id")
            }
        }
    )

    if (uiState.isLoading) {
        LoadingOverlay(modifier = Modifier, message = "Cargando...")
    }
}
