package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.data.enum.EnumModoOperacion
import com.michambita.domain.model.Movimiento
import com.michambita.navigation.Screen
import com.michambita.ui.components.*
import com.michambita.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Estado del formulario
    var modoOperacion by remember { mutableStateOf(EnumModoOperacion.REGISTRAR) }
    var movimientoEditando by remember { mutableStateOf<Movimiento?>(null) }
    var tipoOperacion by remember { mutableStateOf("V") } // "V" = venta, "G" = gasto
    var titulo by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }

    // Configuración del BottomSheet
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )
    val scope = rememberCoroutineScope()

    // Precargar datos en modo edición
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.isVisible && modoOperacion == EnumModoOperacion.EDITAR) {
            movimientoEditando?.let {
                titulo = it.descripcion
                monto = it.monto.toPlainString()
                tipoOperacion = it.tipoMovimiento
            }
        }
    }

    // Obtener movimientos del ViewModel
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    // Funciones de manejo de eventos
    val onGuardarMovimiento: () -> Unit = {
        if (titulo.isNotBlank() && monto.isNotBlank()) {
            val montoDecimal = monto.trim().toBigDecimalOrNull()
            if (montoDecimal != null) {
                when (modoOperacion) {
                    EnumModoOperacion.REGISTRAR -> {
                        val nuevoMovimiento = Movimiento(
                            descripcion = titulo.trim(),
                            monto = montoDecimal,
                            tipoMovimiento = tipoOperacion
                        )
                        viewModel.addMovimiento(nuevoMovimiento)
                    }
                    EnumModoOperacion.EDITAR -> {
                        movimientoEditando?.let { mov ->
                            val movimientoActualizado = mov.copy(
                                descripcion = titulo.trim(),
                                monto = montoDecimal,
                                tipoMovimiento = tipoOperacion
                            )
                            viewModel.updateMovimiento(movimientoActualizado)
                        }
                        movimientoEditando = null
                    }
                }

                // Resetear valores
                scope.launch {
                    scaffoldState.bottomSheetState.hide()
                    titulo = ""
                    monto = ""
                    modoOperacion = EnumModoOperacion.REGISTRAR
                }
            }
        }
    }

    val onRegistrarVenta: () -> Unit = {
        tipoOperacion = "V"
        modoOperacion = EnumModoOperacion.REGISTRAR
        scope.launch { scaffoldState.bottomSheetState.expand() }
    }

    val onRegistrarGasto: () -> Unit = {
        tipoOperacion = "G"
        modoOperacion = EnumModoOperacion.REGISTRAR
        scope.launch { scaffoldState.bottomSheetState.expand() }
    }

    val onEditarMovimiento: (Movimiento) -> Unit = { movimiento ->
        titulo = movimiento.descripcion
        monto = movimiento.monto.toPlainString()
        tipoOperacion = movimiento.tipoMovimiento
        modoOperacion = EnumModoOperacion.EDITAR
        movimientoEditando = movimiento
        scope.launch { scaffoldState.bottomSheetState.expand() }
    }

    val onEliminarMovimiento: (Movimiento) -> Unit = { movimiento ->
        viewModel.deleteMovimiento(movimiento)
    }

    // UI principal
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            MovimientoSheet(
                modoOperacion = modoOperacion,
                tipoOperacion = tipoOperacion,
                titulo = titulo,
                monto = monto,
                onTituloChange = { titulo = it },
                onMontoChange = { monto = it },
                onGuardarClick = onGuardarMovimiento
            )
        }
    ) { padding ->
        HomeContent(
            navController = navController,
            modifier = Modifier.padding(padding),
            movimientosPendientes = movimientos,
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento
        )
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    movimientosPendientes: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit
) {
    val ventas = "S/ 250.75"
    val gastos = "S/ 85.20"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Resumen diario
        ResumenDiario(ventas = ventas, gastos = gastos)

        // Acciones
        HomeAcciones(
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onProductosClick = { navController.navigate(Screen.Producto.route) },
            onInventarioClick = { navController.navigate(Screen.Inventario.route) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Título historial + botón sincronizar
        EncabezadoHistorial()

        // Historial
        MovimientoHistorial(
            movimientosPendientes = movimientosPendientes,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento
        )
    }
}