package com.michambita.feature.inventario.intentmodel

sealed interface InventarioIntent {
    data object LoadProductos : InventarioIntent
    data class UpdateStock(val productoId: String, val newStock: Int) : InventarioIntent
}
