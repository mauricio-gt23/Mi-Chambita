# 📱 MiChambita

**MiChambita** es una aplicación Android nativa en Kotlin, pensada para **microempresarios informales y emprendedores jóvenes (sin RUC)**. Brinda una herramienta sencilla y moderna para **gestionar productos, inventario y movimientos de entrada/salida**, con modo offline-first y sincronización con Firebase.

---

## 🚀 Funcionalidades principales

### 💰 Gestión de Movimientos (Ventas y Gastos)

- ✅ Registro de **movimientos rápidos**: captura de ventas o gastos con monto y descripción
- ✅ Registro de **movimientos detallados**: ventas con múltiples productos (items), cantidades y precios
- ✅ Clasificación automática por tipo: `VENTA` o `GASTO`
- ✅ Historial completo de movimientos con fecha de registro
- ✅ Edición y eliminación de movimientos
- ✅ Modo offline: todos los movimientos se guardan localmente primero
- ✅ Sincronización en segundo plano vía WorkManager

### 📦 Gestión de Inventario

- ✅ Catálogo de productos con nombre, descripción y precio
- ✅ Control de stock por producto (cantidad disponible)
- ✅ Clasificación por tipo de producto (`EnumTipoProducto`)
- ✅ Unidades de medida personalizables (unidad, kg, litro, etc.)
- ✅ Gestión de imágenes: subida y almacenamiento en Firebase Storage
- ✅ Grid de inventario con diálogos de ajuste de stock

### 🔐 Autenticación

- ✅ Firebase Authentication por email
- ✅ Registro en 3 pasos (datos personales, tipo de negocio, empresa)
- ✅ Gestión de sesión de usuario (`SessionViewModel`)
- ✅ Splash con verificación automática de autenticación

### 👤 Perfil

- ✅ Pantalla de perfil con datos del usuario y empresa
- ✅ Cierre de sesión con confirmación

---

## 🏪 Tipos de negocio

Durante el registro (paso 3), el usuario elige el tipo de negocio que maneja. La app adapta la interfaz según esa elección:

| Tipo | Enum | Descripción en registro | Funcionalidades disponibles |
|------|------|-------------------------|---------------------------|
| **Vendo productos** | `INVENTORY` | "Administra tu inventario y registra ventas" | Registrar ingresos/gastos · **Catálogo de productos** · **Inventario con stock** · Selector de productos en movimientos · Historial · Estadísticas |
| **Ofrezco servicios** | `SERVICE` | "Gestiona tus servicios y citas" | Registrar ingresos/gastos · Historial · Estadísticas |
| **Solo ingresos/gastos** | `CASH_FLOW` | "Registra tus ingresos y gastos de forma simple" | Registrar ingresos/gastos · Historial · Estadísticas |

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Kotlin | 1.9.23 | Lenguaje principal |
| Jetpack Compose | BOM 2024.03.00 | UI declarativa |
| Material 3 | 1.2.1 | Sistema de diseño |
| Hilt (Dagger) | 2.48 | Inyección de dependencias (vía kapt) |
| Room | 2.6.1 | Base de datos local |
| DataStore | 1.1.0 | Preferencias de usuario |
| Firebase Auth | 22.3.1 | Autenticación |
| Firebase Firestore | 24.10.3 | Sincronización de datos en la nube |
| Firebase Storage | 20.2.1 | Almacenamiento de imágenes |
| Coil | 2.6.0 | Carga y caché de imágenes |
| Retrofit | 2.9.0 | Cliente HTTP (preparado para backend propio) |
| Navigation Compose | 2.7.6 | Navegación entre pantallas |
| WorkManager | 2.9.0 | Sincronización en segundo plano |
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
                 └─ :common   ← UiState, Screen (rutas), MVI base, DateUtils
  └─ :data                    ← Room, Firebase, DataStore, Retrofit, Workers
       └─ :domain, :common
```

### Módulos

| Módulo | Responsabilidad |
|--------|----------------|
| `:app` | Entry point Android, `@HiltAndroidApp`, WorkManager init |
| `:common` | `UiState`, `Screen` (rutas), `BaseIntentModel` (MVI), `DateUtils` |
| `:domain` | Modelos de negocio, interfaces de repositorio, use cases |
| `:data` | Implementaciones: Room DB, Firebase Auth/Firestore/Storage, DataStore, Retrofit, Workers |
| `:ui` | Tema Material 3, componentes Compose reutilizables, Coil |
| `:router` | `NavigationGraph`, `MainContainer`, `MainViewModel` |
| `:feature:auth` | Login, Registro (3 pasos), Splash, `SessionViewModel` |
| `:feature:home` | Dashboard principal, resumen diario, historial de movimientos |
| `:feature:producto` | CRUD de productos, gestión de imágenes, formulario |
| `:feature:inventario` | Grid de inventario, diálogos de stock, MVI completo |
| `:feature:profile` | Pantalla de perfil, logout |

> Para detalles de dependencias entre módulos y convenciones, ver [AGENTS.md](AGENTS.md).

---

## 📱 Pantallas implementadas

| Pantalla | Ruta | Descripción |
|----------|------|-------------|
| Splash | `splash` | Verificación de sesión → redirige a Login o Home |
| Login | `login` | Autenticación por email con Firebase |
| Registro | `registro` | Registro en 3 pasos (datos, tipo de negocio, empresa) |
| Home | `main_container` | Dashboard con resumen diario e historial de movimientos |
| Producto | `producto` / `producto/{id}` | Formulario de creación/edición con imágenes |
| Inventario | `inventario` | Grid de productos con ajuste de stock |
| Profile | `profile` | Datos del usuario, logout |

---

## 🎨 Diseño y UI

- **Material 3** con tema personalizado en `MiChambitaTheme`
- Soporte de tema **claro/oscuro** vía `isSystemInDarkTheme()`
- Soporte de **Dynamic Color** (Material You) en Android 12+
- Paleta de colores personalizada (tonos naranja/marrón)
- Tipografía y formas (`MiChambitaTypography`, `MiChambitaShapes`)
- Componentes reutilizables: `TopBarScaffold`, `LoadingOverlay`, `ErrorDisplay`, `AlertModal`, `SearchBar`

---

## 🔄 Modo offline y sincronización

- Los movimientos se guardan primero en **Room** (`SynchronizationDB`, `MovimientoEntity`)
- `SyncMovimientosWorker` sincroniza con Firestore en segundo plano vía **WorkManager**
- Preferencias de usuario y tipo de negocio persisten en **DataStore**

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

> **Versión:** 1.0
> **Estado:** En desarrollo activo
