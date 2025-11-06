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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.michambita.ui.components.widget.SearchBar
import com.michambita.ui.components.inventario.item.ItemGrid

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InventarioContent(
    productos: List<Producto>,
    modifier: Modifier = Modifier,
    onAddProduct: () -> Unit = {}
) {
    val gridState = rememberLazyGridState()
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
            var selectedUnidad by remember { mutableStateOf<String?>(null) }
            var onlyIntangibles by remember { mutableStateOf(false) }
            var sortAsc by remember { mutableStateOf(true) }

            SearchBar(query = query, onQueryChange = { query = it })

            val unidades = remember(productos) {
                productos.map { it.unidadMedida }
                    .filter { it.isNotBlank() }
                    .distinct()
            }

            FilterChipsRow(
                unidades = unidades,
                selectedUnidad = selectedUnidad,
                onUnidadSelected = { selectedUnidad = if (selectedUnidad == it) null else it },
                onlyIntangibles = onlyIntangibles,
                onToggleIntangibles = { onlyIntangibles = !onlyIntangibles },
                sortAsc = sortAsc,
                onToggleSort = { sortAsc = !sortAsc }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val filtered = remember(query, selectedUnidad, onlyIntangibles, sortAsc, productos) {
                var list = productos
                    .filter {
                        it.nombre.contains(query, true) || (it.descripcion?.contains(query, true) == true)
                    }
                    .filter { selectedUnidad?.let { u -> it.unidadMedida == u } ?: true }
                    .filter { if (onlyIntangibles) it.esIntangible else true }

                list = if (sortAsc) list.sortedBy { it.precio } else list.sortedByDescending { it.precio }
                list
            }

            ItemGrid(productos = filtered, state = gridState)
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
    unidades: List<String>,
    selectedUnidad: String?,
    onUnidadSelected: (String) -> Unit,
    onlyIntangibles: Boolean,
    onToggleIntangibles: () -> Unit,
    sortAsc: Boolean,
    onToggleSort: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = onlyIntangibles,
                onClick = onToggleIntangibles,
                label = { Text("Intangibles") },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
        item {
            FilterChip(
                selected = !onlyIntangibles,
                onClick = onToggleIntangibles,
                label = { Text("Tangibles") },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
        items(unidades.size) { idx ->
            val unidad = unidades[idx]
            FilterChip(
                selected = selectedUnidad == unidad,
                onClick = { onUnidadSelected(unidad) },
                label = { Text(unidad) },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
        item {
            FilterChip(
                selected = true,
                onClick = onToggleSort,
                label = { Text(if (sortAsc) "Precio ↑" else "Precio ↓") },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
    }
}