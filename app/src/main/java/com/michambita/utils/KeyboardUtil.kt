package com.michambita.utils

import androidx.compose.foundation.clickable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * Oculta el teclado desde un Composable usando LocalSoftwareKeyboardController.
 */
@Composable
fun rememberKeyboardHider(): () -> Unit {
    val controller = LocalSoftwareKeyboardController.current
    return remember { { controller?.hide() } }
}

/**
 * Envuelve un contenido Composable para permitir ocultar el teclado al tocar fuera.
 */
@Composable
fun DismissKeyboardWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        content()
    }
}