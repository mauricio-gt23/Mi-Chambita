package com.michambita.domain.exception

data class StockShortage(
    val itemName: String,
    val available: Int,
    val requested: Int
)

class InsufficientStockException(
    val shortages: List<StockShortage>
) : Exception("Stock insuficiente para ${shortages.size} item(s)")
