package com.michambita.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Producto
import com.michambita.domain.usecase.SaveProductoUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val saveProductoUseCase: SaveProductoUseCase
) : ViewModel() {

    private val _uiStateSaveProducto = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateSaveProducto : StateFlow<UiState<Boolean>> = _uiStateSaveProducto

    fun saveProducto(producto: Producto) {
        viewModelScope.launch {
            _uiStateSaveProducto.value = UiState.Loading

            val result = saveProductoUseCase.invoke(producto)

            _uiStateSaveProducto.value = result.fold(
                onSuccess = { UiState.Success(result.getOrNull() != null) },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al enviar el correo") }
            )
        }
    }
}