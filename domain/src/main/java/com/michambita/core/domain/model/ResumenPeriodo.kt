package com.michambita.domain.model

import java.math.BigDecimal

data class ResumenPeriodo(
    val totalIngresos: BigDecimal,
    val totalGastos: BigDecimal,
    val balance: BigDecimal
) {
    val isBalancePositive: Boolean get() = balance >= BigDecimal.ZERO
}
