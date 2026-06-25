package com.michambita.feature.inventario.components

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michambita.domain.enums.ItemType
import com.michambita.domain.model.Item
import com.michambita.feature.inventario.components.item.ItemGrid
import com.michambita.feature.inventario.components.item.StockDialog
import com.michambita.ui.components.widget.SearchBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InventarioContent(
        items: List<Item>,
        modifier: Modifier = Modifier,
        onAddItem: () -> Unit = {},
        onChangeStock: (String, Int) -> Unit = { _, _ -> },
        onOpenEditItem: (Item) -> Unit = {}
) {
    val gridState = rememberLazyGridState()
    var stockDialogOpen by remember { mutableStateOf(false) }
    var selectedItemId by remember { mutableStateOf<String?>(null) }
    var inputStock by remember { mutableStateOf("") }

    Scaffold(
            floatingActionButton = {
                val fabVisible by remember { derivedStateOf { !gridState.isScrollInProgress } }
                AddItemFab(visible = fabVisible, onClick = onAddItem)
            }
    ) { _ ->
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            InventoryHeader()

            Spacer(modifier = Modifier.height(12.dp))

            // Búsqueda y filtros
            var query by remember { mutableStateOf("") }
            var selectedTipo by remember { mutableStateOf<ItemType?>(null) }

            SearchBar(query = query, onQueryChange = { query = it })

            // TODO: REVISAR IMPLEMENTACION DE FILTROS
            // FilterChipsRow(selectedTipo = selectedTipo, onTipoSelected = { selectedTipo = it })

            Spacer(modifier = Modifier.height(8.dp))

            val filtered =
                    remember(query, selectedTipo, items) {
                        items
                                .filter {
                                    it.nombre.contains(query, true) ||
                                            (it.descripcion?.contains(query, true) == true)
                                }
                                .filter { selectedTipo?.let { tp -> it.itemType == tp } ?: true }
                    }

            ItemGrid(
                    items = filtered,
                    state = gridState,
                    onRequestEditStock = { item ->
                        selectedItemId = item.id
                        inputStock = (item.stock ?: 0).toString()
                        stockDialogOpen = true
                    },
                    onOpenEditProduct = onOpenEditItem
            )

            if (stockDialogOpen) {
                StockDialog(
                        selectedItemId = selectedItemId,
                        inputStock = inputStock,
                        onInputStockChange = { inputStock = it },
                        onConfirm = { id, ns -> onChangeStock(id, ns) },
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
private fun AddItemFab(visible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
    ) {
        FloatingActionButton(onClick = onClick) {
            Icon(Icons.Rounded.Add, contentDescription = "Agregar item")
        }
    }
}

@Composable
fun FilterChipsRow(selectedTipo: ItemType?, onTipoSelected: (ItemType?) -> Unit) {
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
        items(ItemType.values().size) { idx ->
            val tipo = ItemType.values()[idx]
            val label =
                    when (tipo) {
                        ItemType.PRODUCT -> "Productos"
                        ItemType.SERVICE -> "Servicios"
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
