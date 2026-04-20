package com.michambita.feature.home.config

import com.michambita.core.domain.motor.BusinessMotor

data class HomeUiConfig(
    val incomeLabel: String,
    val expenseLabel: String,
    val showInventorySection: Boolean,
    val showProductPicker: Boolean
) {
    companion object {
        fun from(motor: BusinessMotor): HomeUiConfig = when (motor) {
            is BusinessMotor.Inventory -> HomeUiConfig(
                incomeLabel = "Venta",
                expenseLabel = "Gasto",
                showInventorySection = true,
                showProductPicker = true,

            )
            is BusinessMotor.Service -> HomeUiConfig(
                incomeLabel = "Servicio",
                expenseLabel = "Gasto",
                showInventorySection = false,
                showProductPicker = false
            )
            is BusinessMotor.CashFlow -> HomeUiConfig(
                incomeLabel = "Ingreso",
                expenseLabel = "Gasto",
                showInventorySection = false,
                showProductPicker = false
            )
        }
    }
}
