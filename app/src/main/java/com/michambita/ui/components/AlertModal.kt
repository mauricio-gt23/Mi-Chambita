package com.michambita.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.michambita.ui.theme.MiChambitaTheme

@Composable
fun AlertModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "No",
    showConfirmButton: Boolean = true,
    showDismissButton: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        },
        text = {
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        },
        modifier = modifier,
        confirmButton = {
            if (showConfirmButton || showDismissButton) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (showDismissButton) {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(
                                dismissButtonText,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    if (showConfirmButton) {
                        TextButton(
                            onClick = {
                                onConfirm()
                            }
                        ) {
                            Text(
                                confirmButtonText,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ReusableAlertDialogPreview() {
    MiChambitaTheme {
        AlertModal(
            onDismissRequest = {},
            onConfirm = {},
            title = "Título de Prueba",
            message = "Este es un mensaje de prueba para el diálogo."
        )
    }
}
