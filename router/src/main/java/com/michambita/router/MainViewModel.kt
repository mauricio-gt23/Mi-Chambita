package com.michambita.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.core.domain.model.User
import com.michambita.core.domain.motor.BusinessMotor
import com.michambita.core.domain.motor.GetCurrentMotorUseCase
import com.michambita.core.domain.usecase.LoadUserUseCase
import com.michambita.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: LoadUserUseCase,
    getCurrentMotorUseCase: GetCurrentMotorUseCase
) : ViewModel() {
    private val _uiStateGetUser = MutableStateFlow<UiState<User>>(UiState.Empty)
    val uiStateGetUser : StateFlow<UiState<User>> = _uiStateGetUser

    /**
     * The current BusinessMotor, resolved from DataStore preferences.
     * Defaults to Inventory if not yet set (safety net).
     */
    val currentMotor: StateFlow<BusinessMotor> = getCurrentMotorUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BusinessMotor.Inventory // Default fallback
        ) as StateFlow<BusinessMotor>

    fun getUser() {
        viewModelScope.launch {
            _uiStateGetUser.value = UiState.Loading

            val result = getUserUseCase.invoke()

            _uiStateGetUser.value = result.fold(
                onSuccess = { user -> UiState.Success(user) },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al obtener el usuario") }
            )
        }
    }
}
