package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.data.enum.EnumTipoProducto
import com.michambita.domain.model.Producto
import com.michambita.domain.usecase.SaveProductoUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val unidadMedida: String = "",
    val tipoProducto: EnumTipoProducto = EnumTipoProducto.INVENTARIABLE,
    val stock: String = "",
    val imagenUrl: String? = null
)

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val saveProductoUseCase: SaveProductoUseCase,
) : ViewModel() {
    private val _uiFormState = MutableStateFlow(ProductoUiState())
    val uiFormState: StateFlow<ProductoUiState> = _uiFormState.asStateFlow()

    private val _uiStateSaveProducto = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateSaveProducto : StateFlow<UiState<Boolean>> = _uiStateSaveProducto

    

    fun updateNombre(value: String) { _uiFormState.value = _uiFormState.value.copy(nombre = value) }
    fun updateDescripcion(value: String) { _uiFormState.value = _uiFormState.value.copy(descripcion = value) }
    fun updatePrecio(value: String) { _uiFormState.value = _uiFormState.value.copy(precio = value) }
    fun updateUnidadMedida(value: String) { _uiFormState.value = _uiFormState.value.copy(unidadMedida = value) }
    fun setTipoProducto(value: EnumTipoProducto) { _uiFormState.value = _uiFormState.value.copy(tipoProducto = value) }
    fun updateStock(value: String) { _uiFormState.value = _uiFormState.value.copy(stock = value) }

    

    fun guardarProducto(imagenUrl: String? = null) {
        val current = _uiFormState.value

        val precioDouble = current.precio.toDoubleOrNull() ?: 0.0
        val stockInt = if (current.tipoProducto == EnumTipoProducto.INVENTARIABLE) current.stock.toIntOrNull() else null
        val producto = Producto(
            nombre = current.nombre.trim(),
            descripcion = current.descripcion.trim().ifEmpty { null },
            precio = precioDouble,
            unidadMedida = current.unidadMedida.trim().ifEmpty { "" },
            tipoProducto = current.tipoProducto,
            stock = stockInt,
            imagenUrl = imagenUrl ?: current.imagenUrl
        )

        viewModelScope.launch {
            _uiStateSaveProducto.value = UiState.Loading

            val result = saveProductoUseCase.invoke(producto)

            _uiStateSaveProducto.value = result.fold(
                onSuccess = {
                    _uiFormState.value = ProductoUiState()
                    UiState.Success(true)
                },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al guardar el producto") }
            )
        }
    }

    

    fun resetForm() { _uiFormState.value = ProductoUiState() }
}