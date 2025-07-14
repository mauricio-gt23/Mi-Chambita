package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michambita.domain.model.Movimiento
import com.michambita.ui.components.MovimientoItem
import com.michambita.utils.DismissKeyboardWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    DismissKeyboardWrapper {
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
        )
        val scope = rememberCoroutineScope()

        var tipoOperacion by remember { mutableStateOf("V") } // "V" = venta, "G" = gasto
        var titulo by remember { mutableStateOf("") }
        var monto by remember { mutableStateOf("") }

        // Lista local de movimientos por sincronizar
        val movimientosPendientes = remember { mutableStateListOf<Movimiento>() }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (tipoOperacion == "V") Icons.Default.PointOfSale else Icons.Default.MoneyOff,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (tipoOperacion == "V") "Registrar Venta" else "Registrar Gasto",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Descripción") },
                        placeholder = {
                            Text(if (tipoOperacion == "V") "Ej: venta de jugos" else "Ej: compra de vasos")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (titulo.isNotBlank() && monto.isNotBlank()) {
                                val montoDecimal = monto.trim().toBigDecimalOrNull()
                                if (montoDecimal != null) {
                                    val nuevoMovimiento = Movimiento(
                                        descripcion = titulo.trim(),
                                        monto = montoDecimal,
                                        tipoMovimiento = tipoOperacion
                                    )
                                    movimientosPendientes.add(0, nuevoMovimiento)
                                    println("Registro local: $nuevoMovimiento")
                                    scope.launch {
                                        scaffoldState.bottomSheetState.hide()
                                        titulo = ""
                                        monto = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (tipoOperacion == "V") "Guardar Venta" else "Guardar Gasto")
                    }
                }
            }
        ) { padding ->
            HomeContentLayoutOnly(
                modifier = Modifier.padding(padding),
                movimientosPendientes = movimientosPendientes,
                onRegistrarVenta = {
                    tipoOperacion = "V"
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                },
                onRegistrarGasto = {
                    tipoOperacion = "G"
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }
            )
        }
    }
}

@Composable
fun HomeContentLayoutOnly(
    modifier: Modifier = Modifier,
    movimientosPendientes: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryTile(
                title = "Ventas de Hoy",
                icon = Icons.Filled.AttachMoney,
                amount = ventas,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                title = "Gastos de Hoy",
                icon = Icons.Filled.MoneyOff,
                amount = gastos,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }

        // Acciones
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton("Registrar Venta", Icons.Filled.AddShoppingCart, onRegistrarVenta, Modifier.weight(1f))
                ActionButton("Registrar Gasto", Icons.Filled.Payment, onRegistrarGasto, Modifier.weight(1f))
            }

            ActionButton("Ver Inventario", Icons.Filled.Inventory, onClick = {}, isSecondary = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Título historial + botón sincronizar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Movimientos por sincronizar", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { /* lógica de sincronización */ }) {
                Icon(Icons.Default.Sync, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sincronizar")
            }
        }

        // Historial
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (movimientosPendientes.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No hay movimientos pendientes.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /* lógica para ir al historial */ }) {
                            Icon(Icons.Default.History, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ver historial")
                        }
                    }
                } else {
                    LazyColumn (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movimientosPendientes) { movimiento ->
                            MovimientoItem(movimiento)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryTile(
    title: String,
    icon: ImageVector,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(36.dp))
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(amount, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
    }
}


@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
) {
    val colors = if (isSecondary) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.filledTonalButtonColors()
    val border = if (isSecondary) ButtonDefaults.outlinedButtonBorder else null

    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = colors,
        border = border,
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 16.sp)
    }
}