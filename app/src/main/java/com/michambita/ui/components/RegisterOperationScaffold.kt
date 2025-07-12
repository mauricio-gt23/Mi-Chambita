package com.michambita.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.utils.DismissKeyboardWrapper
import com.michambita.utils.rememberKeyboardHider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterOperationScaffold(
    titulo: String,
    etiquetaBoton: String,
    placeholderDescripcion: String,
    icono: ImageVector = Icons.Default.Payment,
    onClose: () -> Unit,
    onGuardar: (monto: String, descripcion: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val hideKeyboard = rememberKeyboardHider()

    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            DismissKeyboardWrapper {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(icono, contentDescription = null)
                        Text(titulo, style = MaterialTheme.typography.titleLarge)
                    }

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        placeholder = { Text(placeholderDescripcion) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            hideKeyboard()
                            onGuardar(monto.trim(), descripcion.trim())
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                                onClose()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(etiquetaBoton)
                    }
                }
            }
        }
    ) {}

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.expand()
    }
}