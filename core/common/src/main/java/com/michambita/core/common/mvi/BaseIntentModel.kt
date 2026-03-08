package com.michambita.core.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseIntentModel<UiState, UiIntent, UiEffect>(
    initialState: UiState
) : ViewModel() {

    // ------------------ STATE
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val currentState: UiState get() = _uiState.value

    // ------------------ INTENT
    private val _intentChannel = Channel<UiIntent>(Channel.UNLIMITED)
    init {
        viewModelScope.launch {
            _intentChannel.consumeAsFlow().collect { intent ->
                handleIntent(intent)
            }
        }
    }
    fun sendIntent(intent: UiIntent) {
        viewModelScope.launch { _intentChannel.send(intent) }
    }
    protected abstract fun handleIntent(intent: UiIntent)

    // ------------------ EFFECT
    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected fun emitEffect(effect: UiEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    // ------------------ REDUCE
    protected fun reduce(reducer: UiState.() -> UiState) {
        _uiState.update { it.reducer() }
    }
}
