# 📱 MiChambita

**MiChambita** es una aplicación Android nativa en Kotlin, pensada para **microempresarios informales y emprendedores jóvenes (sin RUC)**. Brinda una herramienta sencilla y moderna para **gestionar productos o servicios, inventario, movimientos de ventas/gastos e historial**, con datos en la nube vía Firebase. La infraestructura offline (Room + WorkManager) existe en el código pero está actualmente deshabilitada (ver [Persistencia y sincronización](#-persistencia-y-sincronización)).

---

## 🚀 Funcionalidades principales

### 💰 Gestión de Movimientos (Ventas y Gastos)

- ✅ Registro de **movimientos rápidos**: captura de ventas o gastos con monto y descripción
- ✅ Registro de **movimientos detallados**: ventas con múltiples items del catálogo, cantidades y precios
- ✅ Clasificación por tipo: `EnumTipoMovimiento.INCOME` (Venta) o `EXPENSE` (Gasto)
- ✅ CRUD online directo contra Firestore (`companies/{companyId}/movements`)
- ✅ Resumen diario en Home con ventas, gastos y **balance** (`ResumenDiario`)
- ✅ **Validación de stock** en ventas detalladas: si no alcanza, se lanza `InsufficientStockException` y se muestra un modal con los faltantes (`StockShortageModal`)
- ✅ Descuento automático de stock al registrar ventas detalladas (negocios tipo `INVENTORY`)
- ✅ Edición y eliminación de movimientos — la edición reutiliza el `MovimientoSheet` compartido (mismo componente en Home e Historial)

### 📜 Historial de Movimientos

- ✅ **Resumen del período**: total de ventas, gastos y balance (`ResumenCard`)
- ✅ Filtros de fecha: **Hoy · Esta semana · Este mes · Personalizado** (con `DateRangePicker`)
- ✅ Filtro por tipo: Todos · Ventas · Gastos
- ✅ Movimientos agrupados por día ("Hoy", "lunes 13 de julio", …)
- ✅ **Paginación infinita** (25 movimientos por página, cursor Firestore)
- ✅ **Swipe para editar/eliminar** — solo disponible para movimientos de la semana actual; editar abre el `MovimientoSheet` compartido

### 📦 Gestión de Catálogo e Inventario

- ✅ Catálogo de **items** con nombre, descripción y precio — un item es un **producto o un servicio** según el tipo de negocio (`ItemType.PRODUCT` / `SERVICE`)
- ✅ Control de stock por producto (cantidad disponible)
- ✅ Unidades de medida personalizables (unidad, kg, litro, etc.) — solo productos
- ✅ Gestión de imágenes: subida y almacenamiento en Firebase Storage — solo productos
- ✅ Grid de inventario con diálogos de ajuste de stock

### 🔐 Autenticación

- ✅ Firebase Authentication por email
- ✅ Registro en 3 pasos (datos personales, tipo de negocio, empresa)
- ✅ Gestión de sesión de usuario (`SessionViewModel`)
- ✅ Splash con verificación automática de autenticación

### 👤 Perfil

- ✅ Pantalla de perfil con datos del usuario y empresa
- ✅ Cierre de sesión con confirmación

### 🌐 Conexión

- ✅ **Detección de conectividad** vía `NetworkState` (contrato en `:common`, implementado con `ConnectivityManager`/`NetworkCallback` sobre redes validadas)
- ✅ **Modal global sin conexión** (`NoConnectionModal`): al quedarse sin internet, un diálogo no descartable bloquea la app y reintenta automáticamente. El caché de Firestore sigue sirviendo datos detrás del modal

---

## 🏪 Tipos de negocio

Durante el registro (paso 3), el usuario elige el tipo de negocio que maneja. La app adapta la interfaz según esa elección (`HomeUiConfig`, `ItemFormUiConfig`):

| Tipo | Enum | Descripción en registro | Funcionalidades disponibles |
|------|------|-------------------------|---------------------------|
| **Vendo productos** | `INVENTORY` | "Administra tu inventario y registra ventas" | Registrar ingresos/gastos · **Catálogo de productos** (imagen, unidad, stock) · **Inventario con stock** · Selector de items en movimientos · Historial |
| **Ofrezco servicios** | `SERVICE` | "Gestiona tus servicios y citas" | Registrar ingresos/gastos · **Catálogo de servicios** (sin stock ni imágenes) · Selector de items en movimientos · Historial |
| **Solo ingresos/gastos** | `CASH_FLOW` | "Registra tus ingresos y gastos de forma simple" | Registrar ingresos/gastos · Historial |

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Kotlin | 1.9.23 | Lenguaje principal |
| Jetpack Compose | BOM 2024.03.00 | UI declarativa |
| Material 3 | 1.2.1 | Sistema de diseño |
| Hilt (Dagger) | 2.48 | Inyección de dependencias (vía kapt) |
| Room | 2.6.1 | Base de datos local (ruta offline, actualmente inactiva) |
| DataStore | 1.1.0 | Preferencias de usuario y empresa |
| Firebase Auth | 22.3.1 | Autenticación |
| Firebase Firestore | 24.10.3 | Persistencia de datos en la nube (flujo principal) |
| Firebase Storage | 20.2.1 | Almacenamiento de imágenes |
| Coil | 2.6.0 | Carga y caché de imágenes |
| Retrofit | 2.9.0 | Declarado en `:data`, **sin uso actual** (networking 100% Firebase) |
| Navigation Compose | 2.7.6 | Navegación entre pantallas |
| WorkManager | 2.9.0 | Sincronización nocturna en segundo plano (**deshabilitada**) |
| ThreeTenABP | 1.4.6 | API de fechas moderna |

- **Mínimo API:** 23 (Android 6.0)
- **Target/Compile SDK:** 34
- **Java target:** 17
- **Gradle:** 8.7 · **AGP:** 8.4.1

---

## 🏗️ Arquitectura multi-módulo

El proyecto usa **convention plugins** en `build-logic/` para centralizar la configuración.

```
:app                          ← Entry point, @HiltAndroidApp, Google Services
  └─ :router                  ← NavigationGraph, MainContainer, MainViewModel
       └─ :feature:*          ← Módulos de funcionalidad (UI)
            └─ :ui            ← Tema (Color, Type, Shape), componentes Compose
            └─ :domain        ← Modelos de negocio, interfaces de repo, use cases
                 └─ :common   ← UiState, Screen (rutas), DateUtils, NetworkState
  └─ :data                    ← Room, Firebase, DataStore, Workers
       └─ :domain, :common
```

### Módulos

| Módulo | Responsabilidad |
|--------|----------------|
| `:app` | Entry point Android, `@HiltAndroidApp`, WorkManager init (deshabilitado) |
| `:common` | `UiState`, `Screen` (rutas), `DateUtils`, `ValidateUtil`, `NetworkState` (contrato de conectividad) |
| `:domain` | Modelos de negocio, interfaces de repositorio, use cases |
| `:data` | Implementaciones: Room DB, Firebase Auth/Firestore/Storage, DataStore, Workers |
| `:ui` | Tema Material 3, componentes Compose reutilizables, Coil |
| `:router` | `NavigationGraph`, `MainContainer`, `MainViewModel` |
| `:feature:auth` | Login, Registro (3 pasos), Splash, `SessionViewModel` |
| `:feature:home` | Dashboard principal, resumen diario con balance, registro/edición de movimientos |
| `:feature:item` | CRUD de items (producto o servicio), gestión de imágenes, formulario adaptado por tipo de negocio |
| `:feature:inventario` | Grid de inventario, diálogos de stock |
| `:feature:profile` | Pantalla de perfil, logout |
| `:feature:history` | Historial de movimientos: filtros de fecha/tipo, paginación, swipe editar/eliminar |

> Para detalles de dependencias entre módulos y convenciones, ver [AGENTS.md](AGENTS.md).

---

## 📱 Pantallas implementadas

| Pantalla | Ruta | Descripción |
|----------|------|-------------|
| Splash | `splash` | Verificación de sesión → redirige a Login o Home |
| Login | `login` | Autenticación por email con Firebase |
| Registro | `registro` | Registro en 3 pasos (datos, tipo de negocio, empresa) |
| Home | `main_container` → `home` | Dashboard con resumen diario (balance) y registro de movimientos |
| Item | `item` / `item/{id}` | Formulario de creación/edición de items con imágenes |
| Inventario | `inventario` | Grid de items con ajuste de stock |
| Historial | `history` | Historial de movimientos con filtros y paginación |
| Profile | `profile` | Datos del usuario, logout |

La navegación tiene **dos niveles**: `NavigationGraph` (Splash → Login/Registro → `MainContainer`) y un `NavHost` anidado dentro de `MainContainer` para las pantallas autenticadas, con una única `TopAppBar` compartida (no hay bottom navigation bar).

---

## 🎨 Diseño y UI

- **Material 3** con tema personalizado en `MiChambitaTheme`
- Soporte de tema **claro/oscuro** vía `isSystemInDarkTheme()`
- Soporte de **Dynamic Color** (Material You) en Android 12+
- Paleta de colores personalizada (tonos naranja/marrón)
- Tipografía y formas (`MiChambitaTypography`, `MiChambitaShapes`)
- Componentes reutilizables en `:ui`: `TopBarScaffold`, `LoadingOverlay`, `ErrorDisplay`, `AlertModal`, `NoConnectionModal` (sin conexión), `SearchBar`, `SnackbackHost`, `RequiredTextField`, `PasswordTextField` (con `PasswordStrengthIndicator`), `DismissKeyboard`
- Componentes de negocio compartidos: `ResumenCard` (resumen ingresos/gastos/balance, usado en Home e Historial), `MovimientoItemCard`, `SwipeMovimientoItemCard` (swipe editar/eliminar), `MovimientoSheet` (registro/edición de movimientos, compartido por Home e Historial)

---

## 🔄 Persistencia y sincronización

**Flujo activo (online-first):**

- Los movimientos se leen y escriben **directamente en Firestore** bajo `companies/{companyId}/movements`
- Home escucha en tiempo real los movimientos del día (snapshot listener vía `GetMovimientosOnlineUseCase`)
- El historial consulta por rango de fechas con paginación por cursor (`getMovimientosHistorial`)
- Preferencias de usuario y empresa (incluido el tipo de negocio) persisten en **DataStore** (`data_preferences`)
- Sin conexión, un `NoConnectionModal` global bloquea la app (el caché persistente de Firestore sigue mostrando datos detrás); ver `NetworkState`/`NetworkStateImpl`

**Infraestructura offline (preservada, deshabilitada):**

- Existe una ruta offline-first: Room (`SynchronizationDB`, tabla `movimiento`) + `SyncMovimientosWorker` (sincronización nocturna entre 0–5 h vía WorkManager)
- Actualmente está **desactivada**: `setupWorkManager()` está comentado en `MiChambitaApp` y los use cases offline no se usan; puede reactivarse en el futuro

---

## 📤 Funcionalidades futuras

- Reactivar el modo offline-first (Room + WorkManager)
- Reportes y estadísticas de ventas/gastos
- Exportación de reportes en PDF o Excel
- Gráficos de tendencias por período
- Control de ventas a crédito y clientes deudores
- Notificaciones de stock bajo
- Backup automático completo
- Categorías personalizadas para gastos
- Multi-moneda

---

> **Versión:** 1.0
> **Estado:** En desarrollo activo
