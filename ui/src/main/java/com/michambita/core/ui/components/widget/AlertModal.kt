package com.michambita.ui.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AlertModal(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "No",
    showConfirmButton: Boolean = true,
    showDismissButton: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
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
                    Row(
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
}
