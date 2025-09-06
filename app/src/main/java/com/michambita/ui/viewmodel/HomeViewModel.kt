package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.AddMovimientoUseCase
import com.michambita.domain.usecase.DeleteMovimientoUseCase
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import com.michambita.domain.usecase.UpdateMovimientoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMovimientoUseCase: GetAllMovimientoUseCase,
    private val addMovimientoUseCase: AddMovimientoUseCase,
    private val updateMovimientoUseCase: UpdateMovimientoUseCase,
    private val deleteMovimientoUseCase: DeleteMovimientoUseCase,
) : ViewModel() {

    val movimientos: StateFlow<List<Movimiento>> =
        getAllMovimientoUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            addMovimientoUseCase(movimiento)
        }
    }

    fun updateMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            updateMovimientoUseCase(movimiento)
        }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            deleteMovimientoUseCase(movimiento)
        }
    }

}