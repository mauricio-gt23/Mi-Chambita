package com.michambita.domain.usecase

import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.ResumenPeriodo
import javax.inject.Inject

class CalcularResumenUseCase @Inject constructor() {
    operator fun invoke(movimientos: List<Movimiento>): ResumenPeriodo {
        val totalIngresos = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.INCOME }
            .sumOf { it.monto }
        val totalGastos = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.EXPENSE }
            .sumOf { it.monto }
        return ResumenPeriodo(
            totalIngresos = totalIngresos,
            totalGastos = totalGastos,
            balance = totalIngresos.subtract(totalGastos)
        )
    }
}
