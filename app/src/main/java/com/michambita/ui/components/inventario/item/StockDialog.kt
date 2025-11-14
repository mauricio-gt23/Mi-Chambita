package com.michambita.ui.components.inventario.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun StockDialog(
    selectedProductId: String?,
    inputStock: String,
    onInputStockChange: (String) -> Unit,
    onConfirm: (String, Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar stock") },
        text = {
            Column {
                OutlinedTextField(
                    value = inputStock,
                    onValueChange = { value ->
                        onInputStockChange(value.filter { c -> c.isDigit() })
                    },
                    label = { Text("Nuevo stock") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val ns = inputStock.toIntOrNull()
                val id = selectedProductId
                if (id != null && ns != null) {
                    onConfirm(id, ns)
                }
                onDismiss()
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}