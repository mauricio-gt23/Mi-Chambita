package com.michambita.feature.inventario.intentmodel

import com.michambita.domain.model.Producto

data class InventarioUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val errorMessage: String? = null
)
