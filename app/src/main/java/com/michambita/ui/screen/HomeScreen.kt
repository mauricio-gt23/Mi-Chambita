package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
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
import com.michambita.utils.DismissKeyboardWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    DismissKeyboardWrapper {
        val scaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()

        var tipoOperacion by remember { mutableStateOf("venta") }
        var descripcion by remember { mutableStateOf("") }
        var monto by remember { mutableStateOf("") }

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
                            imageVector = if (tipoOperacion == "venta") Icons.Default.PointOfSale else Icons.Default.MoneyOff,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (tipoOperacion == "venta") "Registrar Venta" else "Registrar Gasto",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        placeholder = {
                            Text(if (tipoOperacion == "venta") "Ej: venta de jugos" else "Ej: compra de vasos")
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
                            println("$tipoOperacion -> monto: $monto | desc: $descripcion")
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                                descripcion = ""
                                monto = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (tipoOperacion == "venta") "Guardar Venta" else "Guardar Gasto")
                    }
                }
            }
        ) { padding ->
            HomeContentLayoutOnly(
                modifier = Modifier.padding(padding),
                onRegistrarVenta = {
                    tipoOperacion = "venta"
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                },
                onRegistrarGasto = {
                    tipoOperacion = "gasto"
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }
            )
        }
    }
}

@Composable
fun HomeContentLayoutOnly(
    modifier: Modifier = Modifier,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit
) {
    val ventas = "S/ 250.75"
    val gastos = "S/ 85.20"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 0.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 📊 Resumen del Día
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

        // 🔘 Botones
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

        // 🧾 Movimientos
        Text(
            text = "Últimos movimientos",
            style = MaterialTheme.typography.titleMedium
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RecentActivityItem("Venta de jugos", "S/ 20.00", true)
            RecentActivityItem("Compra de vasos", "S/ 8.50", false)
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

@Composable
fun RecentActivityItem(titulo: String, monto: String, esVenta: Boolean) {
    val color = if (esVenta) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val icon = if (esVenta) Icons.Filled.PointOfSale else Icons.Filled.Receipt

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, fontWeight = FontWeight.SemiBold)
                Text(monto, color = color)
            }
        }
    }
}