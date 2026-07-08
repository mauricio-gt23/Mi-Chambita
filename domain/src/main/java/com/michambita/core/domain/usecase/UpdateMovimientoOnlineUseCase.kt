package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.enums.ItemType
import com.michambita.domain.exception.InsufficientStockException
import com.michambita.domain.exception.StockShortage
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateMovimientoOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val itemRepository: ItemRepository,
) {
    suspend operator fun invoke(
        movimiento: Movimiento,
        originalItems: List<MovimientoItem> = emptyList()
    ): Result<Unit> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id ?: return Result.failure(Exception("El ID de la empresa no es válido"))

        if (company.businessType == BusinessType.INVENTORY &&
            movimiento.tipoMovimiento == EnumTipoMovimiento.INCOME &&
            !movimiento.esMovimientoRapido
        ) {
            val allItemIds = (movimiento.items.map { it.itemId } + originalItems.map { it.itemId }).distinct()
            val freshItemsResult = itemRepository.getItemsByIds(allItemIds)
            if (freshItemsResult.isFailure) {
                return Result.failure(freshItemsResult.exceptionOrNull() ?: Exception("Error al cargar items para validación de stock"))
            }
            val freshItems = freshItemsResult.getOrThrow()
            val freshItemsMap = freshItems.associateBy { it.id }

            val shortages = mutableListOf<StockShortage>()
            allItemIds.forEach { itemId ->
                val newQty = movimiento.items.find { it.itemId == itemId }?.cantidad ?: 0
                val origQty = originalItems.find { it.itemId == itemId }?.cantidad ?: 0
                
                val freshItem = freshItemsMap[itemId]
                if (freshItem != null && freshItem.itemType == ItemType.PRODUCT) {
                    val currentStock = freshItem.stock ?: 0
                    val simulatedStock = currentStock + origQty
                    if (simulatedStock < newQty) {
                        shortages.add(StockShortage(freshItem.nombre, simulatedStock, newQty))
                    }
                }
            }

            if (shortages.isNotEmpty()) {
                return Result.failure(InsufficientStockException(shortages))
            }

            val updateResult = movimientoRepository.updateMovimientoOnline(movimiento, companyId)
            if (updateResult.isFailure) {
                return updateResult
            }

            allItemIds.forEach { itemId ->
                val newQty = movimiento.items.find { it.itemId == itemId }?.cantidad ?: 0
                val origQty = originalItems.find { it.itemId == itemId }?.cantidad ?: 0
                val delta = newQty - origQty
                if (delta != 0) {
                    val freshItem = freshItemsMap[itemId]
                    if (freshItem != null && freshItem.itemType == ItemType.PRODUCT) {
                        val currentStock = freshItem.stock ?: 0
                        val newStock = currentStock - delta
                        itemRepository.updateItemStock(itemId, newStock)
                    }
                }
            }

            return Result.success(Unit)
        } else {
            return movimientoRepository.updateMovimientoOnline(movimiento, companyId)
        }
    }
}
