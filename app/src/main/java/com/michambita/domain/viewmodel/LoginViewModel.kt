package com.michambita.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.usecase.LoginUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState

    fun loginConCorreo(email: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = loginUseCase.loginWithEmail(email)

            _uiState.value = result.fold(
                onSuccess = { UiState.Success("Correo enviado con éxito") },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al enviar el correo") }
            )
        }
    }

    fun loginConCelular() {
        // Lógica para enviar SMS
    }
}