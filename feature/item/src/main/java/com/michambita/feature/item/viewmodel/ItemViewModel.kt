package com.michambita.feature.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.EnumModoOperacion
import com.michambita.domain.enums.ItemType
import com.michambita.domain.model.Item
import com.michambita.domain.usecase.SaveItemUseCase
import com.michambita.domain.usecase.GetItemUseCase
import com.michambita.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val unidadMedida: String = "",
    val itemType: ItemType = ItemType.PRODUCT,
    val stock: String = "",
    val imagenUrl: String? = null
)

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val saveItemUseCase: SaveItemUseCase,
    private val getItemUseCase: GetItemUseCase,
) : ViewModel() {
    private val _uiFormState = MutableStateFlow(ItemUiState())
    val uiFormState: StateFlow<ItemUiState> = _uiFormState.asStateFlow()

    private val _uiStateSaveItem = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateSaveItem: StateFlow<UiState<Boolean>> = _uiStateSaveItem

    private val _uiStateLoadItem = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val uiStateLoadItem: StateFlow<UiState<Unit>> = _uiStateLoadItem.asStateFlow()

    private var currentItemId: String? = null
    private val _modoOperacion = MutableStateFlow(EnumModoOperacion.REGISTRAR)
    val modoOperacion: StateFlow<EnumModoOperacion> = _modoOperacion.asStateFlow()

    fun updateNombre(value: String) {
        _uiFormState.value = _uiFormState.value.copy(nombre = value)
    }

    fun updateDescripcion(value: String) {
        _uiFormState.value = _uiFormState.value.copy(descripcion = value)
    }

    fun updatePrecio(value: String) {
        _uiFormState.value = _uiFormState.value.copy(precio = value)
    }

    fun updateUnidadMedida(value: String) {
        _uiFormState.value = _uiFormState.value.copy(unidadMedida = value)
    }

    fun setItemType(value: ItemType) {
        _uiFormState.value = _uiFormState.value.copy(itemType = value)
    }

    fun updateStock(value: String) {
        _uiFormState.value = _uiFormState.value.copy(stock = value)
    }

    fun resetForm() {
        _uiFormState.value = ItemUiState()
        currentItemId = null
        _modoOperacion.value = EnumModoOperacion.REGISTRAR
    }

    fun guardarItem(imagenUrl: String? = null) {
        val current = _uiFormState.value

        val precioDouble = current.precio.toDoubleOrNull() ?: 0.0
        val stockInt = if (current.itemType == ItemType.PRODUCT) current.stock.toIntOrNull() else null
        val item = Item(
            id = currentItemId,
            nombre = current.nombre.trim(),
            descripcion = current.descripcion.trim().ifEmpty { null },
            precio = precioDouble,
            unidadMedida = if (current.itemType == ItemType.PRODUCT) current.unidadMedida.trim().ifEmpty { null } else null,
            itemType = current.itemType,
            stock = stockInt,
            imagenUrl = if (current.itemType == ItemType.PRODUCT) (imagenUrl ?: current.imagenUrl) else null
        )

        viewModelScope.launch {
            _uiStateSaveItem.value = UiState.Loading

            val result = saveItemUseCase.invoke(item)

            _uiStateSaveItem.value = result.fold(
                onSuccess = {
                    if (_modoOperacion.value == EnumModoOperacion.REGISTRAR) {
                        _uiFormState.value = ItemUiState()
                        currentItemId = null
                    }
                    UiState.Success(true)
                },
                onFailure = {
                    UiState.Error(
                        it.message ?: "Ocurrió un error al guardar"
                    )
                }
            )
        }
    }

    fun cargarItem(id: String) {
        viewModelScope.launch {
            _uiStateLoadItem.value = UiState.Loading
            val result = getItemUseCase.invoke(id)
            _uiStateLoadItem.value = result.fold(
                onSuccess = { p ->
                    currentItemId = p.id
                    _modoOperacion.value = EnumModoOperacion.EDITAR
                    _uiFormState.value = ItemUiState(
                        nombre = p.nombre,
                        descripcion = p.descripcion ?: "",
                        precio = p.precio.toString(),
                        unidadMedida = p.unidadMedida ?: "",
                        itemType = p.itemType,
                        stock = p.stock?.toString() ?: "",
                        imagenUrl = p.imagenUrl
                    )
                    UiState.Success(Unit)
                },
                onFailure = {
                    UiState.Error(it.message ?: "No se pudo cargar el item")
                }
            )
        }
    }

    fun clearLoadState() {
        _uiStateLoadItem.value = UiState.Empty
    }
}
