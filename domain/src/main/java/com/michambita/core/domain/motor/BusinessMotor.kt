package com.michambita.domain.motor

import com.michambita.domain.enums.BusinessType

sealed interface BusinessMotor {
    val businessType: BusinessType

    data object Inventory : BusinessMotor {
        override val businessType = BusinessType.INVENTORY
    }

    data object Service : BusinessMotor {
        override val businessType = BusinessType.SERVICE
    }

    data object CashFlow : BusinessMotor {
        override val businessType = BusinessType.CASH_FLOW
    }

    companion object {
        fun fromType(type: BusinessType): BusinessMotor = when (type) {
            BusinessType.INVENTORY -> Inventory
            BusinessType.SERVICE -> Service
            BusinessType.CASH_FLOW -> CashFlow
        }
    }
}
