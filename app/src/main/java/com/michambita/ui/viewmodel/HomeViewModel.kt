package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.data.enums.EnumTipoMovimiento
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import com.michambita.domain.usecase.SyncMovimientosUseCase
import com.michambita.ui.common.UiState
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
import com.michambita.utils.DateUtils
import kotlinx.coroutines.delay

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
    private val synchronizationRepository: SynchronizationRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    val movimientos: StateFlow<List<Movimiento>> =
        getAllMovimientoUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            movimientos.collect { listaMovimientos ->
                actualizarResumen(listaMovimientos)
            }
        }

        viewModelScope.launch {
            val firstData = getAllMovimientoUseCase().first()
            delay(1000)
            _homeUiState.update { it.copy(isInitialLoading = false) }
        }

        viewModelScope.launch {
            synchronizationRepository.getAllMovimientoPendientes().collect { pendientes ->
                // Filtrar solo movimientos del día anterior
                val yesterday = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                val pendientesAyer = pendientes.filter { movimiento ->
                    DateUtils.isSameDay(movimiento.fechaRegistro, yesterday.time)
                }
                _homeUiState.update { it.copy(movimientosPendientesAyer = pendientesAyer.size) }
            }
        }
    }

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
        val movimientosHoy = movimientos.filter { DateUtils.isToday(it.fechaRegistro) }

        val totalVentas = movimientosHoy
            .filter { it.tipoMovimiento == EnumTipoMovimiento.VENTA }
            .sumOf { it.monto }

        val totalGastos = movimientosHoy
            .filter { it.tipoMovimiento == EnumTipoMovimiento.GASTO }
            .sumOf { it.monto }

        _homeUiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${totalVentas.toPlainString()}",
                gastos = "S/ ${totalGastos.toPlainString()}"
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
