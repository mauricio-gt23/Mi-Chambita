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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

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
}