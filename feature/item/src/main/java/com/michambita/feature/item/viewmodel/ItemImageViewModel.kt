package com.michambita.feature.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.net.Uri
import com.michambita.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.michambita.domain.usecase.UploadItemImageUseCase
import com.michambita.domain.usecase.DeleteItemImageUseCase

@HiltViewModel
class ItemImageViewModel @Inject constructor(
    private val uploadItemImageUseCase: UploadItemImageUseCase,
    private val deleteItemImageUseCase: DeleteItemImageUseCase
) : ViewModel() {
    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    private val _uiStateUploadImage = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val uiStateUploadImage: StateFlow<UiState<Boolean>> = _uiStateUploadImage.asStateFlow()

    fun subirImagen(uri: Uri) {
        viewModelScope.launch {
            _imageUrl.value = uri.toString()
            _uiStateUploadImage.value = UiState.Loading

            val result = uploadItemImageUseCase.invoke(uri.toString())

            _uiStateUploadImage.value = result.fold(
                onSuccess = { url ->
                    _imageUrl.value = url
                    UiState.Success(true)
                },
                onFailure = { e ->
                    _imageUrl.value = null
                    UiState.Error(e.message ?: "Error al subir imagen")
                }
            )
        }
    }

    fun borrarImagen() {
        val currentUrl = _imageUrl.value
        if (currentUrl.isNullOrEmpty()) {
            _uiStateUploadImage.value = UiState.Empty
            return
        }
        viewModelScope.launch {
            _uiStateUploadImage.value = UiState.Loading
            val result = deleteItemImageUseCase.invoke(currentUrl)
            _uiStateUploadImage.value = result.fold(
                onSuccess = {
                    _imageUrl.value = null
                    UiState.Empty
                },
                onFailure = { e ->
                    UiState.Error(e.message ?: "Error al borrar imagen")
                }
            )
        }
    }
}
