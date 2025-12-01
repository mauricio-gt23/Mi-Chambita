import com.michambita.domain.model.MovimientoItem
import java.math.BigDecimal

data class MovimientoItemModel(
    val productoId: String = "",
    val cantidad: Int = 0,
    val precioTotal: BigDecimal = BigDecimal.ZERO
)

fun MovimientoItem.toModel() = MovimientoItemModel(
    productoId,
    cantidad,
    precioTotal
)
