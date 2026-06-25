package com.michambita.feature.inventario.intentmodel

import androidx.lifecycle.viewModelScope
import com.michambita.common.mvi.BaseIntentModel
import com.michambita.domain.usecase.LoadAllItemsByUserIdUseCase
import com.michambita.domain.usecase.UpdateItemStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioIntentModel @Inject constructor(
    private val loadAllItemsByUserIdUseCase: LoadAllItemsByUserIdUseCase,
    private val updateItemStockUseCase: UpdateItemStockUseCase
) : BaseIntentModel<InventarioUiState, InventarioIntent, Nothing>(
    initialState = InventarioUiState()
) {

    override fun handleIntent(intent: InventarioIntent) {
        when (intent) {
            is InventarioIntent.LoadItems -> loadItems()
            is InventarioIntent.UpdateStock -> updateStock(intent.itemId, intent.newStock)
        }
    }

    private fun loadItems() {
        reduce { copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = loadAllItemsByUserIdUseCase.invoke()

            result.fold(
                onSuccess = { items ->
                    reduce { copy(isLoading = false, items = items) }
                },
                onFailure = { error ->
                    reduce { copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    private fun updateStock(itemId: String, newStock: Int) {
        viewModelScope.launch {
            val result = updateItemStockUseCase.invoke(itemId, newStock)

            result.fold(
                onSuccess = {
                    sendIntent(InventarioIntent.LoadItems)
                },
                onFailure = { /* noop */ }
            )
        }
    }
}
