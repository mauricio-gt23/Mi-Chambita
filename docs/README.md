# 📱 MiChambita

**MiChambita** es una aplicación Android desarrollada en Kotlin, pensada para **microempresarios informales y emprendedores jóvenes (sin RUC)**. Brinda una herramienta sencilla y moderna para **gestionar ventas, gastos e inventario de productos**, con modo offline y sincronización con la nube.

---

## 🚀 Funcionalidades principales

### 💰 Gestión de Movimientos (Ventas y Gastos)

- ✅ Registro de **movimientos rápidos**: captura rápida de ventas o gastos con monto y descripción
- ✅ Registro de **movimientos detallados**: ventas con múltiples productos (items), cantidades y precios
- ✅ Clasificación automática por tipo: `VENTA` o `GASTO`
- ✅ Historial completo de movimientos con fecha de registro
- ✅ Modo offline: todos los movimientos se guardan localmente primero

### 📦 Gestión de Inventario

- ✅ Catálogo de productos con nombre, descripción y precio
- ✅ Control de stock por producto (cantidad disponible)
- ✅ Clasificación por tipo de producto
- ✅ Unidades de medida personalizables (unidad, kg, litro, etc.)
- ✅ **Gestión de imágenes**: subida y almacenamiento de fotos de productos en Firebase Storage

### 🔐 Autenticación

- ✅ Firebase Authentication por email
- ✅ Gestión de sesión de usuario
- ✅ Pantalla de splash con verificación de autenticación

### 📊 Interfaz de Usuario

- ✅ Pantalla principal (Home) con resumen de actividad
- ✅ Pantalla de inventario con listado de productos
- ✅ Formulario de productos con captura de imágenes

---

## 🛠️ Tecnologías y arquitectura

- **Lenguaje:** Kotlin 1.9.23
- **Arquitectura:** MVVM (ViewModel + StateFlow)
- **UI:** Jetpack Compose + Material 3
- **Persistencia local:**
  - Room 2.6.1 (base de datos local con entidades: `MovimientoEntity`)
  - DataStore 1.1.0 (para configuración y preferencias de usuario)
- **Inyección de dependencias:** Hilt 2.48
- **Firebase:**
  - Authentication (gestión de usuarios)
  - Firestore (sincronización de datos en la nube)
  - Storage (almacenamiento de imágenes de productos)
- **Gestión de imágenes:** Coil (carga y caché de imágenes)
- **Manejo de fechas:** ThreeTenABP (API de fechas moderna)
- **Preparado para migración a:** Spring Boot (vía Retrofit 2.9.0)
- **Mínimo API:** 23 (Android 6.0)
- **Target SDK:** 34

---

## 📱 Pantallas implementadas

1. **SplashScreen**: Pantalla de inicio con verificación de autenticación
2. **AuthScreen**: Pantalla de login/registro con Firebase
3. **HomeScreen**: Pantalla principal con resumen de movimientos
4. **InventarioScreen**: Listado completo de productos con imágenes
5. **ProductoScreen**: Formulario para crear/editar productos con captura de imagen

---

## 🎨 Diseño y UI

- Material 3 (Material You) con diseño moderno
- Tema claro/oscuro basado en `isSystemInDarkTheme()`
- Archivo `MiChambitaTheme.kt` define:
  - Paleta de colores (`primary`, `secondary`, `background`)
  - Tipografía personalizada (`Typography`)
  - Formas (`Shapes`)
- Componentes reutilizables con Jetpack Compose
- Wrapper para cerrar teclado automáticamente (`DismissKeyboardWrapper`)

---

## 🧭 Navegación

- `Navigation-Compose` con rutas definidas mediante `sealed class Screen`
- Rutas implementadas:
  - `main_container`: Contenedor principal
  - `splash`: Pantalla de inicio
  - `login`: Autenticación
  - `home`: Pantalla principal
  - `inventario`: Gestión de inventario
  - `producto`: Formulario de productos
- Soporte para backstack y restauración de estado
- Integración con Hilt Navigation Compose

---

## 🔄 Modo offline

- Base de datos local: `SynchronizationDB` con DAO para movimientos

---

## 📤 Funcionalidades futuras

- Reportes y estadísticas de ventas/gastos
- Exportación de reportes en PDF o Excel
- Gráficos de tendencias por período
- Control de ventas a crédito y clientes deudores
- Notificaciones de stock bajo
- Backup automático completo
- Categorías personalizadas para gastos
- Multi-moneda

---

## 📦 Dependencias principales

Ver archivo [DEPENDENCIES.md](DEPENDENCIES.md) para el listado completo de dependencias y versiones.

**Principales:**

- Jetpack Compose BOM 2024.03.00
- Room 2.6.1
- Hilt 2.48
- Firebase (Auth, Firestore, Storage)
- Coil (imágenes)
- Retrofit 2.9.0
- ThreeTenABP

---

## 📚 Documentación adicional

- [DEPENDENCIES.md](DEPENDENCIES.md): Listado completo de dependencias y versiones
- [GOOD_PRACTICES_CODE.md](GOOD_PRACTICES_CODE.md): Mejores prácticas de código para el proyecto

---

> **Última actualización:** Diciembre 2024  
> **Versión:** 1.0  
> **Estado:** En desarrollo activo
