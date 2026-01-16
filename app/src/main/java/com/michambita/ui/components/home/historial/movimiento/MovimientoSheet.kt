package com.michambita.ui.components.home.historial.movimiento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.data.enums.EnumModoOperacion
import com.michambita.data.enums.EnumTipoProducto
import com.michambita.data.enums.EnumTipoMovimiento
import com.michambita.domain.model.Producto
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.model.Movimiento
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun MovimientoSheet(
    modifier: Modifier,
    modoOperacion: EnumModoOperacion,
    movimiento: Movimiento?,
    productos: List<Producto> = emptyList(),
    onMovimientoChange: (Movimiento) -> Unit,
    onGuardarClick: () -> Unit,
) {
    val m = movimiento ?: Movimiento(
        descripcion = "",
        monto = BigDecimal.ZERO,
        tipoMovimiento = EnumTipoMovimiento.VENTA,
        esMovimientoRapido = true,
        items = emptyList()
    )

    val tipoOperacion = m.tipoMovimiento

    val showDetalleVenta = tipoOperacion == EnumTipoMovimiento.VENTA && !m.esMovimientoRapido

    val heighSheet = if (showDetalleVenta)  Modifier.fillMaxHeight(0.9f)   else Modifier.height(320.dp)

    Column(
        modifier = heighSheet
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderRow(modoOperacion, tipoOperacion, m.esMovimientoRapido) { checked ->
            onMovimientoChange(
                m.copy(
                    esMovimientoRapido = checked,
                    monto = BigDecimal.ZERO,
                    items = emptyList()
                )
            )
        }

        DescriptionField(m.descripcion, { nuevoTitulo ->
            onMovimientoChange(m.copy(descripcion = nuevoTitulo))
        }, tipoOperacion)

        Box(modifier = Modifier.weight(1f)) {
            if (showDetalleVenta) {
                VentaDetalleSection(
                    itemsIniciales = m.items,
                    productos = productos,
                    onMontoChange = { nuevoMontoStr ->
                        val nuevoMonto =
                            nuevoMontoStr.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                        onMovimientoChange(m.copy(monto = nuevoMonto))
                    },
                    onItemsChange = { nuevosItems ->
                        val total = nuevosItems.fold(BigDecimal.ZERO) { acc, it -> acc + it.precioTotal }
                        onMovimientoChange(m.copy(items = nuevosItems, monto = total))
                    },
                    modoOperacion = modoOperacion,
                    containerModifier = Modifier.fillMaxSize()
                )
            } else {
                MontoField(m.monto.toPlainString()) { nuevoMontoStr ->
                    val nuevoMonto = nuevoMontoStr.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                    onMovimientoChange(m.copy(monto = nuevoMonto))
                }
            }
        }

        if (showDetalleVenta) {
            FooterDetailed(m.monto.toPlainString(), onGuardarClick, modoOperacion, tipoOperacion)
        } else {
            FooterSimple(onGuardarClick, modoOperacion, tipoOperacion)
        }
    }
}

@Composable
private fun HeaderRow(
    modoOperacion: EnumModoOperacion,
    tipoOperacion: EnumTipoMovimiento,
    esMovimientoRapido: Boolean,
    onVentaRapidaChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (tipoOperacion == EnumTipoMovimiento.VENTA) Icons.Default.PointOfSale else Icons.Default.MoneyOff,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (modoOperacion == EnumModoOperacion.REGISTRAR) {
                    if (tipoOperacion == EnumTipoMovimiento.VENTA) "Registrar Venta" else "Registrar Gasto"
                } else {
                    "Editar Movimiento"
                },
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (tipoOperacion == EnumTipoMovimiento.VENTA) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = esMovimientoRapido,
                    onCheckedChange = onVentaRapidaChange,
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DescriptionField(
    titulo: String,
    onTituloChange: (String) -> Unit,
    tipoOperacion: EnumTipoMovimiento
) {
    OutlinedTextField(
        value = titulo,
        onValueChange = onTituloChange,
        label = { Text("Descripción") },
        placeholder = { Text(if (tipoOperacion == EnumTipoMovimiento.VENTA) "Ej: venta de jugos" else "Ej: compra de vasos") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MontoField(
    monto: String,
    onMontoChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = monto,
        onValueChange = onMontoChange,
        label = { Text("Monto") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun VentaDetalleSection(
    itemsIniciales: List<MovimientoItem>,
    productos: List<Producto>,
    onMontoChange: (String) -> Unit,
    onItemsChange: (List<MovimientoItem>) -> Unit,
    modoOperacion: EnumModoOperacion,
    containerModifier: Modifier = Modifier,
) {
    Column(modifier = containerModifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        var cantidad by remember { mutableStateOf("") }
        var precioUnitario by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var selectedProducto by remember { mutableStateOf<Producto?>(null) }
        var itemsVenta by remember { mutableStateOf(itemsIniciales) }
        var showDetalle by remember { mutableStateOf(modoOperacion == EnumModoOperacion.EDITAR || itemsIniciales.isNotEmpty()) }

        LaunchedEffect(Unit) {
            if (itemsIniciales.isNotEmpty()) {
                val total = itemsIniciales.fold(BigDecimal.ZERO) { acc, item -> acc + item.precioTotal }
                onMontoChange(total.toString())
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedProducto?.nombre ?: "",
                onValueChange = {},
                label = { Text("Producto") },
                placeholder = { Text("Selecciona producto") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                productos.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p.nombre) },
                        onClick = {
                            selectedProducto = p
                            expanded = false
                            precioUnitario = p.precio.toString()
                            val c = cantidad.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                            val total = c.multiply(p.precio.toBigDecimal())
                            onMontoChange(total.toPlainString())
                        }
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (selectedProducto?.tipoProducto == EnumTipoProducto.INVENTARIABLE) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = {
                        cantidad = it
                        val c = it.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                        val p = precioUnitario.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                        val total = c.multiply(p)
                        onMontoChange(total.toPlainString())
                    },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = precioUnitario,
                onValueChange = {
                    precioUnitario = it
                    val c = cantidad.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                    val p = it.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                    val total = c.multiply(p)
                    onMontoChange(total.toPlainString())
                },
                label = { Text("Precio unitario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val p = selectedProducto
                    val precio = precioUnitario.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                    if (p != null && precio > BigDecimal.ZERO) {
                        val cantInt = if (p.tipoProducto == EnumTipoProducto.INVENTARIABLE) {
                            cantidad.toIntOrNull() ?: 0
                        } else {
                            1
                        }
                        if (p.tipoProducto != EnumTipoProducto.INVENTARIABLE || cantInt > 0) {
                            val totalItem = precio.multiply(cantInt.toBigDecimal())
                            itemsVenta = itemsVenta + MovimientoItem(p.id ?: "", cantInt, totalItem)
                            val total = itemsVenta.fold(BigDecimal.ZERO) { acc, item ->
                                acc + item.precioTotal
                            }
                            onMontoChange(total.toString())
                            cantidad = ""
                            onItemsChange(itemsVenta)
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Agregar a venta") }

            OutlinedButton(
                onClick = { showDetalle = !showDetalle },
                modifier = Modifier.weight(1f)
            ) { Text(if (showDetalle) "Ocultar detalle" else "Ver detalle") }
        }

        if (showDetalle && itemsVenta.isNotEmpty()) {
            VentaDetalleList(itemsVenta, productos, onMontoChange, Modifier.fillMaxSize()) {
                itemsVenta = it
                onItemsChange(itemsVenta)
            }
        }
    }
}

@Composable
private fun VentaDetalleList(
    itemsVenta: List<MovimientoItem>,
    productos: List<Producto>,
    onMontoChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onItemsUpdate: (List<MovimientoItem>) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Productos en la venta", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    itemsVenta,
                    key = { idx, item -> "${item.productoId}:${item.cantidad}:${item.precioTotal.toPlainString()}:$idx" }) { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            val nombre = productos.find { it.id == item.productoId }?.nombre
                                ?: item.productoId
                            val unitPrice = if (item.cantidad > 0) {
                                item.precioTotal.divide(
                                    BigDecimal(item.cantidad),
                                    2,
                                    RoundingMode.HALF_UP
                                )
                            } else {
                                BigDecimal.ZERO
                            }
                            Text(nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "${item.cantidad} x S/ ${unitPrice.toPlainString()}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            text = "S/ ${item.precioTotal.toPlainString()}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = {
                            val updated = itemsVenta.toMutableList()
                            if (index in 0 until updated.size) {
                                updated.removeAt(index)
                                onItemsUpdate(updated)
                                val total = updated.fold(BigDecimal.ZERO) { acc, it2 -> acc + it2.precioTotal }
                                onMontoChange(total.toString())
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FooterDetailed(
    monto: String,
    onGuardarClick: () -> Unit,
    modoOperacion: EnumModoOperacion,
    tipoOperacion: EnumTipoMovimiento
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = monto,
            onValueChange = {},
            label = { Text("Total") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = false,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                when (modoOperacion) {
                    EnumModoOperacion.REGISTRAR ->
                        if (tipoOperacion == EnumTipoMovimiento.VENTA) "Guardar Venta" else "Guardar Gasto"

                    EnumModoOperacion.EDITAR -> "Guardar Cambios"
                }
            )
        }
    }
}

@Composable
private fun FooterSimple(
    onGuardarClick: () -> Unit,
    modoOperacion: EnumModoOperacion,
    tipoOperacion: EnumTipoMovimiento
) {
    Button(
        onClick = onGuardarClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            when (modoOperacion) {
                EnumModoOperacion.REGISTRAR ->
                    if (tipoOperacion == EnumTipoMovimiento.VENTA) "Guardar Venta" else "Guardar Gasto"

                EnumModoOperacion.EDITAR -> "Guardar Cambios"
            }
        )
    }
}
