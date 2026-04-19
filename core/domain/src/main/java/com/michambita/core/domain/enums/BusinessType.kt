package com.michambita.core.domain.enums

/**
 * Defines the type of business the user operates.
 * Selected once during empresa creation (onboarding) and drives
 * the motor-based behavior throughout the app.
 */
enum class BusinessType {
    INVENTORY,   // "Vendo productos" — inventory + product catalog + multi-item movements
    SERVICE,     // "Ofrezco servicios" — single service per movement, no inventory
    CASH_FLOW    // "Solo registro ingresos/gastos" — simplest: amount + description only
}
