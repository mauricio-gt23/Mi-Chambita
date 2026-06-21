package com.michambita.feature.item.config

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.enums.ItemType

data class ItemFormUiConfig(
    val showImageSection: Boolean,
    val showUnidadMedida: Boolean,
    val showStock: Boolean,
    val itemTypeLabel: String,  // "Producto" o "Servicio"
    val itemType: ItemType
) {
    companion object {
        fun from(businessType: BusinessType): ItemFormUiConfig = when (businessType) {
            BusinessType.INVENTORY -> ItemFormUiConfig(
                showImageSection = true,
                showUnidadMedida = true,
                showStock = true,
                itemTypeLabel = "Producto",
                itemType = ItemType.PRODUCT
            )
            BusinessType.SERVICE -> ItemFormUiConfig(
                showImageSection = false,
                showUnidadMedida = false,
                showStock = false,
                itemTypeLabel = "Servicio",
                itemType = ItemType.SERVICE
            )
            else -> throw IllegalArgumentException("BusinessType $businessType no soporta Items")
        }
    }
}
