package com.michambita.ui.components.inventario

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Producto
import com.michambita.data.enum.EnumTipoProducto
import com.michambita.ui.components.widget.SearchBar
import com.michambita.ui.components.inventario.item.ItemGrid
import com.michambita.ui.components.inventario.item.StockDialog

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InventarioContent(
    productos: List<Producto>,
    modifier: Modifier = Modifier,
    onAddProduct: () -> Unit = {},
    onChangeStock: (String, Int) -> Unit = { _, _ -> }
) {
    val gridState = rememberLazyGridState()
    var stockDialogOpen by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }
    var inputStock by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            val fabVisible by remember { derivedStateOf { !gridState.isScrollInProgress } }
            AddProductFab(visible = fabVisible, onClick = onAddProduct)
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            InventoryHeader()

            Spacer(modifier = Modifier.height(12.dp))

            // Búsqueda y filtros
            var query by remember { mutableStateOf("") }
            var selectedTipo by remember { mutableStateOf<EnumTipoProducto?>(null) }

            SearchBar(query = query, onQueryChange = { query = it })

            FilterChipsRow(
                selectedTipo = selectedTipo,
                onTipoSelected = { selectedTipo = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val filtered = remember(query, selectedTipo, productos) {
                var list = productos
                    .filter {
                        it.nombre.contains(query, true) || (it.descripcion?.contains(query, true) == true)
                    }
                    .filter { selectedTipo?.let { tp -> it.tipoProducto == tp } ?: true }
                list
            }

            ItemGrid(
                productos = filtered,
                state = gridState,
                onRequestEditStock = { p ->
                    selectedProductId = p.id
                    inputStock = (p.stock ?: 0).toString()
                    stockDialogOpen = true
                }
            )

            if (stockDialogOpen) {
                StockDialog(
                    selectedProductId = selectedProductId,
                    inputStock = inputStock,
                    onInputStockChange = { inputStock = it },
                    onConfirm = { id, ns ->
                        onChangeStock(id, ns)
                    },
                    onDismiss = { stockDialogOpen = false }
                )
            }
        }
    }
}

@Composable
private fun InventoryHeader(title: String = "Inventario") {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun AddProductFab(visible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        FloatingActionButton(onClick = onClick) {
            Icon(Icons.Rounded.Add, contentDescription = "Agregar producto")
        }
    }
}



@Composable
fun FilterChipsRow(
    selectedTipo: EnumTipoProducto?,
    onTipoSelected: (EnumTipoProducto?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedTipo == null,
                onClick = { onTipoSelected(null) },
                label = { Text("Todos") },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
        items(EnumTipoProducto.values().size) { idx ->
            val tipo = EnumTipoProducto.values()[idx]
            val label = when (tipo) {
                EnumTipoProducto.INVENTARIABLE -> "Inventariable"
                EnumTipoProducto.NO_INVENTARIABLE -> "No inventariable"
                EnumTipoProducto.SERVICIO -> "Servicio"
            }
            FilterChip(
                selected = selectedTipo == tipo,
                onClick = { onTipoSelected(tipo) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
    }
}