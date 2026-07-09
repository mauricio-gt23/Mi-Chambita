package com.michambita.ui.components.widget

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

@Composable
fun RequiredTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        modifier: Modifier = Modifier,
        isRequired: Boolean = true,
        customError: String? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        maxLines: Int = 1,
        singleLine: Boolean = true,
        placeholder: @Composable (() -> Unit)? = null,
        defaultSupportingText: @Composable (() -> Unit)? = null,
        onFocusChanged: ((Boolean) -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    var wasFocused by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Reset validation state when value is cleared externally (e.g., after form save)
    LaunchedEffect(value) {
        if (value.isEmpty() && !isFocused) {
            wasFocused = false
            showError = false
        }
    }

    val hasError = (value.isBlank() && isRequired) || customError != null
    val isErrorActive = showError && hasError && (!isFocused || customError != null)

    val errorMessage =
            when {
                value.isBlank() && isRequired -> "Este campo es requerido"
                customError != null -> customError
                else -> null
            }

    val labelText = buildAnnotatedString {
        append(label)
        if (isRequired) {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) { append(" *") }
        }
    }

    OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                if (wasFocused) {
                    showError = true
                }
            },
            label = { Text(text = labelText) },
            placeholder = placeholder,
            isError = isErrorActive,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            singleLine = singleLine,
            supportingText =
                    if (isErrorActive && errorMessage != null) {
                        { Text(text = errorMessage) }
                    } else {
                        defaultSupportingText
                    },
            modifier =
                    modifier.onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (focusState.isFocused) {
                            wasFocused = true
                        } else if (wasFocused) {
                            showError = true
                        }
                        onFocusChanged?.invoke(focusState.isFocused)
                    }
    )
}
