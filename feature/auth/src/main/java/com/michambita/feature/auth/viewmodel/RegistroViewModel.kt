package com.michambita.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Company
import com.michambita.domain.model.User
import com.michambita.domain.usecase.RegisterUseCase
import com.michambita.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistroUiState(
    val usuario: User = User(),
    val currentStep: Int = 1,
    val companyOption: String = "crear",
    val company: Company = Company(nombre = ""),
    val businessType: BusinessType? = null
)

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _registroUiState = MutableStateFlow(RegistroUiState())
    val registroUiState: StateFlow<RegistroUiState> = _registroUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    /**
     * Returns the total number of steps based on the company option.
     */
    val totalSteps: Int
        get() = if (_registroUiState.value.companyOption == "crear") 3 else 2

    fun register() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val state = _registroUiState.value
            val usuario = state.usuario
            val companyOption = state.companyOption
            val company = state.company

            val result = registerUseCase(
                name = usuario.name ?: "",
                email = usuario.email ?: "",
                password = usuario.password ?: "",
                companyOption = companyOption,
                companyName = if (companyOption == "crear") company.nombre else null,
                companyCode = if (companyOption == "asociar") company.id else null,
                businessType = if (companyOption == "crear") state.businessType else null
            )

            result.fold(
                onSuccess = { registerResult ->
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

    fun updateCompanyOption(option: String) {
        _registroUiState.value = _registroUiState.value.copy(companyOption = option)
    }

    fun updateCompanyNombre(nombre: String) {
        _registroUiState.value = _registroUiState.value.copy(
            company = _registroUiState.value.company.copy(nombre = nombre)
        )
    }

    fun updateCompanyCodigo(codigo: String) {
        _registroUiState.value = _registroUiState.value.copy(
            company = _registroUiState.value.company.copy(id = codigo)
        )
    }

    fun updateBusinessType(type: BusinessType) {
        _registroUiState.value = _registroUiState.value.copy(businessType = type)
    }

    fun clearError() {
        _uiState.value = UiState.Empty
    }
}