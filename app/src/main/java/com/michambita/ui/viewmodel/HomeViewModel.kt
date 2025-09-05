package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMovimientoUseCase: GetAllMovimientoUseCase,
) : ViewModel() {

    val movimientos: StateFlow<List<Movimiento>> =
        getAllMovimientoUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


}