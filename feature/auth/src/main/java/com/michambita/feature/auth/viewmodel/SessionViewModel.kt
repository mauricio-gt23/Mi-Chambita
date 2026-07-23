package com.michambita.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.BusinessType
import com.michambita.common.network.NetworkState
import com.michambita.domain.usecase.GetBusinessTypeUseCase
import com.michambita.domain.usecase.LoadUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val getBusinessTypeUseCase: GetBusinessTypeUseCase,
    private val networkState: NetworkState
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkState.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val userSessionState: StateFlow<UserSessionState> = loadUserUseCase.getCurrentUserId()
        .map { userUid ->
            if (userUid != null) UserSessionState.Authenticated
            else UserSessionState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSessionState.Unknown
        )

    val currentBusinessType: StateFlow<BusinessType?> = getBusinessTypeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}

sealed class UserSessionState {
    object Unknown : UserSessionState()
    object Authenticated : UserSessionState()
    object Unauthenticated : UserSessionState()
}