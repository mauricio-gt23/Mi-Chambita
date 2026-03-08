package com.michambita.feature.inventario.intentmodel

import androidx.lifecycle.viewModelScope
import com.michambita.core.common.mvi.BaseIntentModel
import com.michambita.core.domain.usecase.LoadAllProductoByUserId
import com.michambita.core.domain.usecase.UpdateProductoStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioIntentModel @Inject constructor(
    private val loadAllProductoByUserId: LoadAllProductoByUserId,
    private val updateProductoStockUseCase: UpdateProductoStockUseCase
) : BaseIntentModel<InventarioUiState, InventarioIntent, Nothing>(
    initialState = InventarioUiState()
) {

    override fun handleIntent(intent: InventarioIntent) {
        when (intent) {
            is InventarioIntent.LoadProductos -> loadProductos()
            is InventarioIntent.UpdateStock -> updateStock(intent.productoId, intent.newStock)
        }
    }

    private fun loadProductos() {
        reduce { copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = loadAllProductoByUserId.invoke()

            result.fold(
                onSuccess = { productos ->
                    reduce { copy(isLoading = false, productos = productos) }
                },
                onFailure = { error ->
                    reduce { copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    private fun updateStock(productoId: String, newStock: Int) {
        viewModelScope.launch {
            val result = updateProductoStockUseCase.invoke(productoId, newStock)

            result.fold(
                onSuccess = {
                    sendIntent(InventarioIntent.LoadProductos)
                },
                onFailure = { /* noop — mismo comportamiento actual */ }
            )
        }
    }
}
