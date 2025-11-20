package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.usecase.AuthUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLogin: Boolean = true
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = authUseCase.login(email, password)

            _uiState.value = result.fold(
                onSuccess = { UiState.Success("Inicio de sesión exitoso") },
                onFailure = { UiState.Error(it.message!!) }
            )
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = authUseCase.register(name, email, password)

            _uiState.value = result.fold(
                onSuccess = { UiState.Success("Registro exitoso") },
                onFailure = { UiState.Error(it.message!!) }
            )
        }
    }

    fun submit() {
        viewModelScope.launch {
            val current = _authUiState.value
            if (current.isLogin) {
                login(current.email, current.password)
            } else {
                register(current.name, current.email, current.password)
            }
        }
    }

    fun updateIsLogin() {
        val current = _authUiState.value
        _authUiState.value = _authUiState.value.copy(isLogin = !current.isLogin)
    }

    fun updateNombre(value: String) {
        _authUiState.value = _authUiState.value.copy(name = value)
    }

    fun updateEmail(value: String) {
        _authUiState.value = _authUiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _authUiState.value = _authUiState.value.copy(password = value)
    }

    fun updateConfirmPassword(value: String) {
        _authUiState.value = _authUiState.value.copy(confirmPassword = value)
    }
}