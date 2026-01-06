package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Empresa
import com.michambita.domain.model.User
import com.michambita.domain.usecase.RegisterUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistroUiState(
    val usuario: User = User(),
    val currentStep: Int = 1,
    val empresaOption: String = "crear",
    val empresa: Empresa = Empresa(nombre = "")
)

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registroUiState = MutableStateFlow(RegistroUiState())
    val registroUiState: StateFlow<RegistroUiState> = _registroUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    fun register() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val usuario = _registroUiState.value.usuario
            val empresaOption = _registroUiState.value.empresaOption
            val empresa = _registroUiState.value.empresa
            
            val result = registerUseCase(
                name = usuario.name ?: "",
                email = usuario.email ?: "",
                password = usuario.password ?: "",
                empresaOption = empresaOption,
                empresaNombre = if (empresaOption == "crear") empresa.nombre else null,
                empresaCodigo = if (empresaOption == "asociar") empresa.id else null
            )

            _uiState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun updateNombre(value: String) {
        _registroUiState.value = _registroUiState.value.copy(
            usuario = _registroUiState.value.usuario.copy(name = value)
        )
    }

    fun updateEmail(value: String) {
        _registroUiState.value = _registroUiState.value.copy(
            usuario = _registroUiState.value.usuario.copy(email = value)
        )
    }

    fun updatePassword(value: String) {
        _registroUiState.value = _registroUiState.value.copy(
            usuario = _registroUiState.value.usuario.copy(password = value)
        )
    }

    fun updateConfirmPassword(value: String) {
        _registroUiState.value = _registroUiState.value.copy(
            usuario = _registroUiState.value.usuario.copy(confirmPassword = value)
        )
    }

    fun updateCurrentStep(step: Int) {
        _registroUiState.value = _registroUiState.value.copy(currentStep = step)
    }

    fun updateEmpresaOption(option: String) {
        _registroUiState.value = _registroUiState.value.copy(empresaOption = option)
    }

    fun updateEmpresaNombre(nombre: String) {
        _registroUiState.value = _registroUiState.value.copy(
            empresa = _registroUiState.value.empresa.copy(nombre = nombre)
        )
    }

    fun updateEmpresaCodigo(codigo: String) {
        _registroUiState.value = _registroUiState.value.copy(
            empresa = _registroUiState.value.empresa.copy(id = codigo)
        )
    }

    fun clearError() {
        _uiState.value = UiState.Empty
    }
}