package com.michambita.core.domain.motor

import com.michambita.core.domain.enums.BusinessType

/**
 * Defines motor-specific behavior. Each BusinessType has a concrete implementation.
 * The motor answers questions like:
 *  - Does this business type need an inventory?
 *  - What label should we show for "income"?
 *  - Should the movement sheet show product selection?
 */
sealed interface BusinessMotor {
    val businessType: BusinessType
    val incomeLabel: String       // Spanish UI label for income
    val expenseLabel: String      // Spanish UI label for expense
    val needsInventory: Boolean
    val needsProductSelection: Boolean
    val supportsMultipleItems: Boolean
    val movementSheetType: MovementSheetType

    data object Inventory : BusinessMotor {
        override val businessType = BusinessType.INVENTORY
        override val incomeLabel = "Venta"
        override val expenseLabel = "Gasto"
        override val needsInventory = true
        override val needsProductSelection = true
        override val supportsMultipleItems = true
        override val movementSheetType = MovementSheetType.INVENTORY
    }

    data object Service : BusinessMotor {
        override val businessType = BusinessType.SERVICE
        override val incomeLabel = "Servicio"
        override val expenseLabel = "Gasto"
        override val needsInventory = false
        override val needsProductSelection = false
        override val supportsMultipleItems = false
        override val movementSheetType = MovementSheetType.SERVICE
    }

    data object CashFlow : BusinessMotor {
        override val businessType = BusinessType.CASH_FLOW
        override val incomeLabel = "Ingreso"
        override val expenseLabel = "Gasto"
        override val needsInventory = false
        override val needsProductSelection = false
        override val supportsMultipleItems = false
        override val movementSheetType = MovementSheetType.CASH_FLOW
    }

    companion object {
        fun fromType(type: BusinessType): BusinessMotor = when (type) {
            BusinessType.INVENTORY -> Inventory
            BusinessType.SERVICE -> Service
            BusinessType.CASH_FLOW -> CashFlow
        }
    }
}

/**
 * Determines the type of movement registration sheet to display.
 */
enum class MovementSheetType {
    INVENTORY,  // Full sheet: product picker, quantities, multi-item
    SERVICE,    // Medium sheet: service description, amount, optional notes
    CASH_FLOW   // Simple sheet: income/expense toggle, amount, description
}
