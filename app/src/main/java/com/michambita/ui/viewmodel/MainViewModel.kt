package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.data.repository.AuthRepository
import com.michambita.domain.model.User
import com.michambita.domain.usecase.LoadUserUseCase
import com.michambita.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserSessionState {
    object Unknown : UserSessionState()
    object Authenticated : UserSessionState()
    object Unauthenticated : UserSessionState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserUseCase: LoadUserUseCase,
) : ViewModel() {

    private val _uiStateGetUser = MutableStateFlow<UiState<User>>(UiState.Empty)
    val uiStateGetUser : StateFlow<UiState<User>> = _uiStateGetUser

    val userSessionState: StateFlow<UserSessionState> = authRepository.getCurrentUser()
        .map { userUid ->
            if (userUid != null) {
                UserSessionState.Authenticated
            } else {
                UserSessionState.Unauthenticated
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSessionState.Unknown
        )

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