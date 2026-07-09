package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.enums.ItemType
import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DeleteMovimientoOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val itemRepository: ItemRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id ?: return Result.failure(Exception("El ID de la empresa no es válido"))

        if (company.businessType == BusinessType.INVENTORY &&
            movimiento.tipoMovimiento == EnumTipoMovimiento.INCOME &&
            !movimiento.esMovimientoRapido &&
            movimiento.items.isNotEmpty()
        ) {
            val deleteResult = movimientoRepository.deleteMovimientoOnline(movimiento, companyId)
            if (deleteResult.isFailure) {
                return deleteResult
            }

            val itemIds = movimiento.items.map { it.itemId }
            val freshItemsResult = itemRepository.getItemsByIds(itemIds)
            if (freshItemsResult.isSuccess) {
                val freshItemsMap = freshItemsResult.getOrThrow().associateBy { it.id }
                movimiento.items.forEach { mItem ->
                    val freshItem = freshItemsMap[mItem.itemId]
                    if (freshItem != null && freshItem.itemType == ItemType.PRODUCT) {
                        val currentStock = freshItem.stock ?: 0
                        val newStock = currentStock + mItem.cantidad
                        itemRepository.updateItemStock(mItem.itemId, newStock)
                    }
                }
            }

            return Result.success(Unit)
        } else {
            return movimientoRepository.deleteMovimientoOnline(movimiento, companyId)
        }
    }
}
