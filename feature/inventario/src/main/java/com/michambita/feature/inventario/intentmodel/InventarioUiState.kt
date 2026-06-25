package com.michambita.feature.inventario.intentmodel

import com.michambita.domain.model.Item

data class InventarioUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null
)
