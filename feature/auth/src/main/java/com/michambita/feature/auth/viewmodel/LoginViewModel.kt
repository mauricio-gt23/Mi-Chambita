package com.michambita.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.core.domain.usecase.LoginUseCase
import com.michambita.domain.usecase.SendPasswordResetUseCase
import com.michambita.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _authUiState = MutableStateFlow(LoginUiState())
    val authUiState: StateFlow<LoginUiState> = _authUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    private val _resetPasswordState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val resetPasswordState: StateFlow<UiState<String>> = _resetPasswordState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = loginUseCase.invoke(email, password)

            _uiState.value = result.fold(
                onSuccess = { UiState.Success("Inicio de sesión exitoso") },
                onFailure = { UiState.Error(it.message!!) }
            )
        }
    }

    fun updateEmail(value: String) {
        _authUiState.value = _authUiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _authUiState.value = _authUiState.value.copy(password = value)
    }

    fun clearError() {
        _uiState.value = UiState.Empty
    }

    fun sendResetPassword(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = UiState.Loading
            _resetPasswordState.value = sendPasswordResetUseCase(email).fold(
                onSuccess = { UiState.Success("Te enviamos un correo para restablecer tu contraseña") },
                onFailure = { UiState.Error(it.message ?: "No se pudo enviar el correo") }
            )
        }
    }

    fun clearResetState() {
        _resetPasswordState.value = UiState.Empty
    }
}