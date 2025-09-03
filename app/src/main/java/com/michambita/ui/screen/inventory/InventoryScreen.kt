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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michambita.domain.model.Producto
import com.michambita.utils.DismissKeyboardWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController) {
    DismissKeyboardWrapper {
        val productos = remember {
            mutableStateListOf(
                    Producto(nombre="Manzana Roja", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Verde", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Negra", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Azul", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Roja", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Verde", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Negra", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Azul", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Roja", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Verde", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Negra", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            Producto(nombre="Manzana Azul", precio= Double.MAX_VALUE, unidadMedida = "120", esIntangible = false ),
            )
        }

        InventoryTable(
            productos = productos,
            onEdit = { producto ->
                // Aquí lógica para editar producto
                println("Editar: $producto")
            },
            onDelete = { producto ->
                productos.remove(producto)
            },
            onSave = {
                println("Guardar inventario")
            },
            onStockClick = {
                println("Actualizar stock")
            }
        )
    }
}