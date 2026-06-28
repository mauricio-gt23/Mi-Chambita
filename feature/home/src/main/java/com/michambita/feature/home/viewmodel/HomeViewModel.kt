package com.michambita.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.repository.SynchronizationRepository
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import com.michambita.domain.usecase.GetMovimientosOnlineUseCase
import com.michambita.domain.usecase.SyncMovimientosUseCase
import com.michambita.common.UiState
import java.util.Calendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.michambita.common.DateUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

data class HomeUiState(
    val ventas: String = "S/ 0.00",
    val gastos: String = "S/ 0.00",
    val bottomSheetVisible: Boolean = false,
    val isInitialLoading: Boolean = true,
    val movimientosPendientesAyer: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMovimientoUseCase: GetAllMovimientoUseCase,
    private val syncMovimientosUseCase: SyncMovimientosUseCase,
    private val synchronizationRepository: SynchronizationRepository,
    private val getMovimientosOnlineUseCase: GetMovimientosOnlineUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    // ── Online-first: lectura de movimientos de hoy desde Firestore ──
    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    init {
        // Online-first: cargar movimientos de hoy desde Firestore
        viewModelScope.launch {
            val flow = getMovimientosOnlineUseCase()
            flow.collect { listaMovimientos ->
                _movimientos.value = listaMovimientos
                actualizarResumen(listaMovimientos)
            }
        }

        viewModelScope.launch {
            delay(1500)
            _homeUiState.update { it.copy(isInitialLoading = false) }
        }

        // ── Offline-first code (preserved for future use) ──────────────
        // viewModelScope.launch {
        //     movimientosOffline.collect { listaMovimientos ->
        //         actualizarResumen(listaMovimientos)
        //     }
        // }
        //
        // viewModelScope.launch {
        //     val firstData = getAllMovimientoUseCase().first()
        //     delay(1000)
        //     _homeUiState.update { it.copy(isInitialLoading = false) }
        // }
        //
        // viewModelScope.launch {
        //     synchronizationRepository.getAllMovimientoPendientes().collect { pendientes ->
        //         val yesterday = Calendar.getInstance().apply {
        //             add(Calendar.DAY_OF_YEAR, -1)
        //         }
        //         val pendientesAyer = pendientes.filter { movimiento ->
        //             DateUtils.isSameDay(movimiento.fechaRegistro, yesterday.time)
        //         }
        //         _homeUiState.update { it.copy(movimientosPendientesAyer = pendientesAyer.size) }
        //     }
        // }
    }

    // ── Offline-first: lectura desde Room (preserved for future use) ──
    // val movimientosOffline: StateFlow<List<Movimiento>> =
    //     getAllMovimientoUseCase()
    //         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSincronizarMovimientos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = syncMovimientosUseCase()

            result.fold(
                onSuccess = {
                    _uiState.value = UiState.Success("Movimientos sincronizados")
                },
                onFailure = {
                    _uiState.value = UiState.Error(it.message ?: "Error al sincronizar")
                }
            )
        }
    }

    private fun actualizarResumen(movimientos: List<Movimiento>) {
        // Online-first: los movimientos ya vienen filtrados por hoy desde Firestore
        val totalIncome = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.INCOME }
            .sumOf { it.monto }

        val totalExpense = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.EXPENSE }
            .sumOf { it.monto }

        _homeUiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${totalIncome.toPlainString()}",
                gastos = "S/ ${totalExpense.toPlainString()}"
            )
        }
    }

    fun showBottomSheet() {
        _homeUiState.update { it.copy(bottomSheetVisible = true) }
    }

    fun hideBottomSheet() {
        _homeUiState.update { it.copy(bottomSheetVisible = false) }
    }

    fun clearUiState() {
        _uiState.value = UiState.Empty
    }
}
