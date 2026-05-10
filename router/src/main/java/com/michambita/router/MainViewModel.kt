package com.michambita.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.User
import com.michambita.domain.motor.BusinessMotor
import com.michambita.domain.usecase.GetCurrentBusinessMotorUseCase
import com.michambita.domain.usecase.LoadUserUseCase
import com.michambita.common.UiState
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
    private val getCurrentBusinessMotorUseCase: GetCurrentBusinessMotorUseCase
) : ViewModel() {
    private val _uiStateGetUser = MutableStateFlow<UiState<User>>(UiState.Empty)
    val uiStateGetUser : StateFlow<UiState<User>> = _uiStateGetUser

    val currentMotor: StateFlow<BusinessMotor> = getCurrentBusinessMotorUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BusinessMotor.CashFlow
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
