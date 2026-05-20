package com.michambita.feature.home.config

import com.michambita.domain.enums.BusinessType

data class HomeUiConfig(
    val showInventorySection: Boolean,
    val showProductPicker: Boolean
) {
    companion object {
        fun from(type: BusinessType): HomeUiConfig = when (type) {
            BusinessType.INVENTORY -> HomeUiConfig(
                showInventorySection = true,
                showProductPicker = true
            )
            BusinessType.SERVICE -> HomeUiConfig(
                showInventorySection = false,
                showProductPicker = false
            )
            BusinessType.CASH_FLOW -> HomeUiConfig(
                showInventorySection = false,
                showProductPicker = false
            )
        }
    }
}
