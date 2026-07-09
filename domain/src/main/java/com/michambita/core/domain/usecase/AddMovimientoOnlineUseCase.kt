package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.enums.ItemType
import com.michambita.domain.exception.InsufficientStockException
import com.michambita.domain.exception.StockShortage
import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddMovimientoOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val itemRepository: ItemRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val user = userPreferencesRepository.userFlow.firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id ?: return Result.failure(Exception("El ID de la empresa no es válido"))

        if (company.businessType == BusinessType.INVENTORY &&
            movimiento.tipoMovimiento == EnumTipoMovimiento.INCOME &&
            !movimiento.esMovimientoRapido &&
            movimiento.items.isNotEmpty()
        ) {
            val itemIds = movimiento.items.map { it.itemId }
            val freshItemsResult = itemRepository.getItemsByIds(itemIds)
            if (freshItemsResult.isFailure) {
                return Result.failure(freshItemsResult.exceptionOrNull() ?: Exception("Error al cargar items para validación de stock"))
            }
            val freshItems = freshItemsResult.getOrThrow()
            val freshItemsMap = freshItems.associateBy { it.id }

            val shortages = mutableListOf<StockShortage>()
            movimiento.items.forEach { mItem ->
                val freshItem = freshItemsMap[mItem.itemId]
                if (freshItem != null && freshItem.itemType == ItemType.PRODUCT) {
                    val availableStock = freshItem.stock ?: 0
                    if (availableStock < mItem.cantidad) {
                        shortages.add(StockShortage(freshItem.nombre, availableStock, mItem.cantidad))
                    }
                }
            }

            if (shortages.isNotEmpty()) {
                return Result.failure(InsufficientStockException(shortages))
            }

            val movimientoToSave = movimiento.copy(
                createdByUserId = user.userId,
                companyId = companyId
            )
            val saveResult = movimientoRepository.addMovimientoOnline(movimientoToSave, companyId)
            if (saveResult.isFailure) {
                return saveResult
            }

            movimiento.items.forEach { mItem ->
                val freshItem = freshItemsMap[mItem.itemId]
                if (freshItem != null && freshItem.itemType == ItemType.PRODUCT) {
                    val availableStock = freshItem.stock ?: 0
                    val newStock = availableStock - mItem.cantidad
                    itemRepository.updateItemStock(mItem.itemId, newStock)
                }
            }

            return Result.success(Unit)
        } else {
            val movimientoToSave = movimiento.copy(
                createdByUserId = user.userId,
                companyId = companyId
            )
            return movimientoRepository.addMovimientoOnline(movimientoToSave, companyId)
        }
    }
}
