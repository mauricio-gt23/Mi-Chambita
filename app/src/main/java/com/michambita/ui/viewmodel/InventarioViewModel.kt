package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Producto
import com.michambita.domain.usecase.LoadAllProductoByUserId
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val loadAllProductoByUserId: LoadAllProductoByUserId
) : ViewModel() {

    private val _uiStateGetAllProducto = MutableStateFlow<UiState<List<Producto>>>(UiState.Empty)
    val uiStateGetAllProducto : StateFlow<UiState<List<Producto>>> = _uiStateGetAllProducto

    fun getAllProducto() {
        viewModelScope.launch {
            _uiStateGetAllProducto.value = UiState.Loading

            val result = loadAllProductoByUserId.invoke()

            _uiStateGetAllProducto.value = result.fold(
                onSuccess = { UiState.Success(result.getOrNull()!!) },
                onFailure = { UiState.Error(it.message!!) }
            )
        }
    }
}