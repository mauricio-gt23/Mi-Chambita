package com.michambita.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.domain.model.Producto
import com.michambita.domain.viewmodel.ProductoViewModel
import com.michambita.ui.common.UiState
import com.michambita.utils.DismissKeyboardWrapper
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductoViewModel = hiltViewModel()
) {
    // Estado UI del ViewModel (lifecycle-aware)
    val uiState by viewModel.uiStateSaveProducto.collectAsStateWithLifecycle()

    // Snackbar host
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Campos locales del formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("") }
    var esIntangible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                Log.d("ProductoScreen", "Producto guardado correctamente")
                snackbarHostState.showSnackbar(
                    message = "Producto guardado correctamente",
                    duration = SnackbarDuration.Short
                )
                // Si quieres limpiar campos automáticamente al guardar:
                nombre = ""
                descripcion = ""
                precio = ""
                unidadMedida = ""
                esIntangible = false
                // Si tu ViewModel expone un método para resetear estado, llamalo aquí:
                // viewModel.resetSaveState()
            }
            is UiState.Error -> {
                val msg = (uiState as UiState.Error).message
                Log.e("ProductoScreen", msg)
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DismissKeyboardWrapper {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Registro de Producto",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Datos del producto", style = MaterialTheme.typography.titleMedium)

                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre del producto") },
                                leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(12.dp))

                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción") },
                                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(12.dp))

                            OutlinedTextField(
                                value = precio,
                                onValueChange = { precio = it },
                                label = { Text("Precio") },
                                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(12.dp))

                            OutlinedTextField(
                                value = unidadMedida,
                                onValueChange = { unidadMedida = it },
                                label = { Text("Unidad de medida") },
                                leadingIcon = { Icon(Icons.Default.Straighten, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = esIntangible, onCheckedChange = { esIntangible = it })
                                Spacer(Modifier.width(8.dp))
                                Text("Producto intangible")
                            }

                            Divider(Modifier.padding(vertical = 12.dp))

                            Text("Imagen del producto", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            Button(
                                onClick = { /* lógica seleccionar imagen */ },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Seleccionar foto del producto")
                            }

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    // Validaciones básicas
                                    if (nombre.isBlank()) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Ingrese el nombre del producto")
                                        }
                                        return@Button
                                    }

                                    val producto = Producto(
                                        id = UUID.randomUUID().toString(),
                                        nombre = nombre.trim(),
                                        descripcion = descripcion.trim().ifEmpty { null },
                                        precio = precio.toDoubleOrNull() ?: 0.0,
                                        unidadMedida = unidadMedida.trim().ifEmpty { "" },
                                        esIntangible = esIntangible,
                                    )
                                    viewModel.saveProducto(producto)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Guardar")
                            }
                        }
                    }
                }
            }

            // Loader centrado cuando el estado es Loading
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}
