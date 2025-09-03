package com.michambita.ui.screen.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Producto

@Composable
fun InventoryTable(
    productos: List<Producto>,
    onEdit: (Producto) -> Unit,
    onDelete: (Producto) -> Unit,
    onSave: () -> Unit,
    onStockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery = remember { mutableStateOf("") }

    val productosFiltrados = productos.filter {
        it.nombre.contains(searchQuery.value, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔍 Buscador + Acciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                label = { Text("Buscar...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            IconButton(onClick = { onStockClick() }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Stock")
            }
            IconButton(onClick = { onSave() }) {
                Icon(Icons.Default.Save, contentDescription = "Guardar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📝 Encabezado de tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Producto", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("Stock", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Precio", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Acciones", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }

        // 📦 Lista de productos
        LazyColumn {
            items(productosFiltrados) { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(producto.nombre, modifier = Modifier.weight(2f))
//                    Text("[ ${producto.stock} ]", modifier = Modifier.weight(1f))
                    Text("$${producto.precio}", modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEdit(producto) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete(producto) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
                Divider()
            }
        }
    }
}
