package com.michambita.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Empresa
import com.michambita.domain.model.User
import com.michambita.domain.usecase.RegisterUseCase
import com.michambita.common.UiState
import com.michambita.domain.repository.preference.BusinessTypePreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistroUiState(
    val usuario: User = User(),
    val currentStep: Int = 1,
    val empresaOption: String = "crear",
    val empresa: Empresa = Empresa(nombre = ""),
    val businessType: BusinessType? = null
)

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val businessTypePreferencesRepository: BusinessTypePreferencesRepository
) : ViewModel() {

    private val _registroUiState = MutableStateFlow(RegistroUiState())
    val registroUiState: StateFlow<RegistroUiState> = _registroUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    /**
     * Returns the total number of steps based on the empresa option.
     */
    val totalSteps: Int
        get() = if (_registroUiState.value.empresaOption == "crear") 3 else 2

    fun register() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val state = _registroUiState.value
            val usuario = state.usuario
            val empresaOption = state.empresaOption
            val empresa = state.empresa

            val result = registerUseCase(
                name = usuario.name ?: "",
                email = usuario.email ?: "",
                password = usuario.password ?: "",
                empresaOption = empresaOption,
                empresaNombre = if (empresaOption == "crear") empresa.nombre else null,
                empresaCodigo = if (empresaOption == "asociar") empresa.id else null,
                businessType = if (empresaOption == "crear") state.businessType else null
            )

            result.fold(
                onSuccess = { registerResult ->
                    businessTypePreferencesRepository.saveBusinessType(registerResult.businessType)
                    _uiState.value = UiState.Success(registerResult.message)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Error desconocido")
                }
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

    fun updateBusinessType(type: BusinessType) {
        _registroUiState.value = _registroUiState.value.copy(businessType = type)
    }

    fun clearError() {
        _uiState.value = UiState.Empty
    }
}