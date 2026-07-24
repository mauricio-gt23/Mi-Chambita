package com.michambita.feature.inventario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Item
import com.michambita.domain.usecase.LoadAllItemsByCompanyIdUseCase
import com.michambita.domain.usecase.UpdateItemStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
data class InventarioUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val loadAllItemsByCompanyIdUseCase: LoadAllItemsByCompanyIdUseCase,
    private val updateItemStockUseCase: UpdateItemStockUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(InventarioUiState())
    val uiState: StateFlow<InventarioUiState> = _uiState.asStateFlow()

    fun loadItems() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = loadAllItemsByCompanyIdUseCase.invoke()

            result.fold(
                onSuccess = { items ->
                    _uiState.update { it.copy(isLoading = false, items = items) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun updateStock(itemId: String, newStock: Int) {
        viewModelScope.launch {
            val result = updateItemStockUseCase.invoke(itemId, newStock)

            result.fold(
                onSuccess = {
                    loadItems()
                },
                onFailure = { /* noop */ }
            )
        }
    }
}
