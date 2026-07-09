package com.michambita.feature.home.config

import com.michambita.domain.enums.BusinessType

data class HomeUiConfig(
    val showInventorySection: Boolean,
    val loadItemList: Boolean,
    val itemLabel: String,
) {
    companion object {
        fun from(type: BusinessType): HomeUiConfig = when (type) {
            BusinessType.INVENTORY -> HomeUiConfig(
                showInventorySection = true,
                loadItemList = true,
                itemLabel = "Productos"
            )
            BusinessType.SERVICE -> HomeUiConfig(
                showInventorySection = true,
                loadItemList = true,
                itemLabel = "Servicios"
            )
            BusinessType.CASH_FLOW -> HomeUiConfig(
                showInventorySection = false,
                loadItemList = false,
                itemLabel = ""
            )
        }
    }
}
