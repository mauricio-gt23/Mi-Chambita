package com.michambita.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.User
import com.michambita.domain.usecase.LoadUserUseCase
import com.michambita.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
) : ViewModel() {
    private val _uiStateLoadUser = MutableStateFlow<UiState<User>>(UiState.Empty)
    val uiStateLoadUser : StateFlow<UiState<User>> = _uiStateLoadUser

    fun loadUser() {
        viewModelScope.launch {
            _uiStateLoadUser.value = UiState.Loading

            val result = loadUserUseCase.invoke()

            _uiStateLoadUser.value = result.fold(
                onSuccess = { user -> UiState.Success(user) },
                onFailure = { UiState.Error(it.message ?: "Ocurrió un error al obtener el usuario") }
            )
        }
    }
}

