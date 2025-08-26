package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class UserSessionState {
    object Unknown : UserSessionState()
    object Authenticated : UserSessionState()
    object Unauthenticated : UserSessionState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

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
}