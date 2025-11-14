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
import android.net.Uri
import com.michambita.domain.usecase.UploadProductoImageUseCase

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
    private val uploadProductoImageUseCase: UploadProductoImageUseCase
) : ViewModel() {
    private val _formState = MutableStateFlow(ProductoUiState())
    val formState: StateFlow<ProductoUiState> = _formState.asStateFlow()

    private val _uiStateSaveProducto = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateSaveProducto : StateFlow<UiState<Boolean>> = _uiStateSaveProducto

    private val _uiStateUploadImage = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateUploadImage: StateFlow<UiState<Boolean>> = _uiStateUploadImage

    fun updateNombre(value: String) { _formState.value = _formState.value.copy(nombre = value) }
    fun updateDescripcion(value: String) { _formState.value = _formState.value.copy(descripcion = value) }
    fun updatePrecio(value: String) { _formState.value = _formState.value.copy(precio = value) }
    fun updateUnidadMedida(value: String) { _formState.value = _formState.value.copy(unidadMedida = value) }
    fun setTipoProducto(value: EnumTipoProducto) { _formState.value = _formState.value.copy(tipoProducto = value) }
    fun updateStock(value: String) { _formState.value = _formState.value.copy(stock = value) }

    fun subirImagen(uri: Uri, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(imagenUrl = uri.toString())
            _uiStateUploadImage.value = UiState.Loading

            val result = uploadProductoImageUseCase.invoke(uri)

            _uiStateUploadImage.value = result.fold(
                onSuccess = { url ->
                    _formState.value = _formState.value.copy(imagenUrl = url)
                    onResult(true)
                    UiState.Success(true)
                },
                onFailure = { e ->
                    _formState.value = _formState.value.copy(imagenUrl = null)
                    onResult(false)
                    UiState.Error(e.message ?: "Error al subir imagen")
                }
            )
        }
    }

    fun guardarProducto() {
        val current = _formState.value

        val precioDouble = current.precio.toDoubleOrNull() ?: 0.0
        val stockInt = if (current.tipoProducto == EnumTipoProducto.INVENTARIABLE) current.stock.toIntOrNull() else null
        val producto = Producto(
            nombre = current.nombre.trim(),
            descripcion = current.descripcion.trim().ifEmpty { null },
            precio = precioDouble,
            unidadMedida = current.unidadMedida.trim().ifEmpty { "" },
            tipoProducto = current.tipoProducto,
            stock = stockInt,
            imagenUrl = current.imagenUrl
        )

        viewModelScope.launch {
            _uiStateSaveProducto.value = UiState.Loading

            val result = saveProductoUseCase.invoke(producto)

            _uiStateSaveProducto.value = result.fold(
                onSuccess = {
                    _formState.value = ProductoUiState()
                    UiState.Success(true)
                },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al guardar el producto") }
            )
        }
    }

    fun borrarImagen() {
        _formState.value = _formState.value.copy(imagenUrl = null)
        _uiStateUploadImage.value = UiState.Empty
    }

    fun resetForm() { _formState.value = ProductoUiState() }
}