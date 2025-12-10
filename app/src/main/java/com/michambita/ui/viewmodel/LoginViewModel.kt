package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.usecase.LoginUseCase
import com.michambita.ui.common.UiState
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
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _authUiState = MutableStateFlow(LoginUiState())
    val authUiState: StateFlow<LoginUiState> = _authUiState

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = loginUseCase.login(email, password)

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
}