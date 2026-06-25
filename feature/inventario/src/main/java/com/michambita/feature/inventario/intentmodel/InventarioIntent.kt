package com.michambita.feature.inventario.intentmodel

sealed interface InventarioIntent {
    data object LoadItems : InventarioIntent
    data class UpdateStock(val itemId: String, val newStock: Int) : InventarioIntent
}
