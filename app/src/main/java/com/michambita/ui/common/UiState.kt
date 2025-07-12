package com.michambita.ui.common

sealed class UiState<out T> {
    object Empty : UiState<Nothing>()
    object  Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}