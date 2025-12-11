package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.usecase.LoginUseCase
import com.michambita.domain.usecase.RegisterUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistroUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val currentStep: Int = 1,
    val empresaOption: String = "crear",
    val empresaNombre: String = "",
    val empresaCodigo: String = ""
)

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registroUiState = MutableStateFlow(RegistroUiState())
    val registroUiState: StateFlow<RegistroUiState> = _registroUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = registerUseCase.invoke(name, email, password)

            _uiState.value = result.fold(
                onSuccess = { UiState.Success("Registro exitoso") },
                onFailure = { UiState.Error(it.message!!) }
            )
        }
    }

    fun updateNombre(value: String) {
        _registroUiState.value = _registroUiState.value.copy(name = value)
    }

    fun updateEmail(value: String) {
        _registroUiState.value = _registroUiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _registroUiState.value = _registroUiState.value.copy(password = value)
    }

    fun updateConfirmPassword(value: String) {
        _registroUiState.value = _registroUiState.value.copy(confirmPassword = value)
    }

    fun updateCurrentStep(step: Int) {
        _registroUiState.value = _registroUiState.value.copy(currentStep = step)
    }

    fun updateEmpresaOption(option: String) {
        _registroUiState.value = _registroUiState.value.copy(empresaOption = option)
    }

    fun updateEmpresaNombre(nombre: String) {
        _registroUiState.value = _registroUiState.value.copy(empresaNombre = nombre)
    }

    fun updateEmpresaCodigo(codigo: String) {
        _registroUiState.value = _registroUiState.value.copy(empresaCodigo = codigo)
    }
}