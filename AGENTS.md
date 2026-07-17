# AGENTS.md — MiChambita

## Project Overview

MiChambita es una app Android nativa para gestión de micro-negocios (chambitas).
Permite registrar items (productos o servicios), controlar inventario, registrar
movimientos de ventas/gastos y consultar el historial. El flujo de datos actual es
**online-first vía Firestore**; la infraestructura offline (Room + WorkManager)
existe pero está deshabilitada (`setupWorkManager()` comentado en `MiChambitaApp`).

## Build System

| Herramienta | Versión | Fuente |
|-------------|---------|--------|
| Kotlin      | 1.9.23  | `libs.versions.toml` → `kotlin` |
| AGP         | 8.4.1   | `libs.versions.toml` → `agp` |
| Gradle      | 8.7     | `gradle-wrapper.properties` |
| Compose BOM | 2024.03.00 | `libs.versions.toml` → `composeBom` |
| Compose Compiler | 1.5.11 | `libs.versions.toml` → `composeCompiler` |
| Java target | 17      | Convention plugins |

### Convention Plugins (`build-logic/convention/`)

Cuatro plugins registrados en `build-logic/convention/build.gradle.kts`:

| Plugin ID | Clase | Qué configura |
|-----------|-------|---------------|
| `michambita.android.application` | `AndroidApplicationConventionPlugin` | `com.android.application` + Kotlin + Hilt + Compose, compileSdk/minSdk/targetSdk 34/23/34, Java 17, test deps |
| `michambita.android.library` | `AndroidLibraryConventionPlugin` | `com.android.library` + Kotlin, compileSdk 34, minSdk 23, Java 17, `buildConfig = false` |
| `michambita.android.hilt` | `AndroidHiltConventionPlugin` | kapt + Dagger Hilt 2.48 (incluye comment para futura migración a KSP) |
| `michambita.android.compose` | `AndroidComposeConventionPlugin` | Compose BOM, Material 3, Navigation Compose, Hilt Navigation, Lifecycle, debug tooling |

**Regla:** Todo módulo nuevo debe usar estos plugins. No configurar `compileSdk`,
`minSdk`, ni `composeOptions` directamente en el `build.gradle.kts` del módulo.

### Version Catalog

Todas las dependencias externas se gestionan vía `gradle/libs.versions.toml`.
El build-logic lo importa como included build desde `build-logic/settings.gradle.kts`.
No usar dependencias hardcodeadas con versión inline; todo debe pasar por el catalog.

## Module Architecture

### Patrón de capas

```
:app                          ← Entry point, wires all modules
  └─ :router                  ← Navigation graph (conoce todos los features)
       └─ :feature:*          ← Feature modules (UI de cada funcionalidad)
            └─ :ui            ← Componentes Compose reutilizables, tema
            └─ :domain        ← Modelos, interfaces de repositorio, use cases
                 └─ :common   ← Utilidades base (UiState, Screen, MVI, DateUtils)
  └─ :data                    ← Implementaciones de repos (Firebase, Room, DataStore)
       └─ :domain, :common
```

### Reglas de dependencia

| Capa origen | Puede depender de | NO puede depender de |
|-------------|-------------------|----------------------|
| `:app` | Todos los módulos | — |
| `:router` | `:feature:*`, `:ui`, `:domain`, `:common` | `:data` |
| `:feature:*` | `:ui`, `:domain`, `:common` | `:data`, `:router`, otros `:feature:*` |
| `:ui` | `:domain` | `:data`, `:common`, `:feature:*` |
| `:data` | `:domain`, `:common` | `:ui`, `:feature:*`, `:router` |
| `:domain` | `:common` | Todo lo demás |
| `:common` | Nada (módulo hoja) | Todo |

## Cross-Module Conventions

### Nombres de módulos y paquetes

- Módulos core: nombre plano (`:common`, `:domain`, `:data`, `:ui`, `:router`)
- Módulos feature: bajo `:feature:<nombre>` con paquete `com.michambita.feature.<nombre>`
- Paquetes core usan `com.michambita.core.<módulo>` (ej. `com.michambita.core.domain`)

### Dependency Injection

- **Hilt** (Dagger) vía kapt en todos los módulos que lo necesitan
- `@HiltAndroidApp` en `:app`, `@AndroidEntryPoint` en Activities/Fragments
- `@HiltViewModel` + `@Inject constructor` en ViewModels
- Módulos de provisión (ej. `RepositoryModule`, `FirebaseModule`) en `:data`

### Patrones de estado UI

Dos patrones coexisten:
- **`UiState<T>`** (sealed class en `:common`): `Empty | Loading | Success<T> | Error(message)`
  — usado en ViewModels que exponen un solo flujo de estado simple, o combinado con
  data classes `*UiState` propias + `MutableStateFlow` (Home, Item, Profile, History).
- **MVI vía `BaseIntentModel<UiState, UiIntent, UiEffect>`** (en `:common/mvi/`):
  State + Intent + Effect con `Channel` + `StateFlow`. Usado **solo** en `:feature:inventario`.

### Navegación

- Rutas definidas como `sealed class Screen` en `:common` (todas las rutas centralizadas).
- Dos `NavHost` anidados en `:router`:
  - `NavigationGraph` — top-level: Splash → Login/Registro → `MainContainer`,
    gated por el estado de sesión y tipo de negocio de `SessionViewModel`.
  - `MainContainer` — NavHost propio para el shell autenticado (Home, Item,
    Inventario, Profile, History) con una única `TopAppBar` compartida.
    No hay bottom navigation bar.

### Linters / Formatters

<!-- NO_EVIDENCE -->
No se encontró configuración de detekt, ktlint, ni spotless en el repositorio.
Tampoco se encontraron archivos de CI (.github/workflows, Jenkinsfile, etc.).

### Testing

- Test runner: `AndroidJUnitRunner` (configurado en convention plugins)
- Dependencias de test provistas por `michambita.android.application`: JUnit 4,
  AndroidX Test Ext, Espresso, Compose UI Test
- No se observan tests escritos en los módulos actuales.

## Module Index

| Módulo | Responsabilidad |
|--------|----------------|
| `:app` | Entry point Android, `@HiltAndroidApp`, Google Services, WorkManager init (deshabilitado) |
| `:common` | Utilidades transversales: `UiState`, `Screen` (rutas), MVI base, `DateUtils`, `ValidateUtil` |
| `:domain` | Modelos de negocio, interfaces de repositorio, use cases |
| `:data` | Implementaciones: Room DB (ruta offline inactiva), Firebase Auth/Firestore/Storage, DataStore, Workers. Retrofit está declarado pero sin uso |
| `:ui` | Tema Material 3 (Color, Type, Shape), componentes Compose reutilizables, Coil |
| `:router` | `NavigationGraph`, `MainContainer`, `MainViewModel` — ensambla la navegación |
| `:feature:auth` | Login, Registro (3 pasos), Splash, `SessionViewModel` |
| `:feature:home` | Dashboard principal, resumen diario con balance, registro/edición de movimientos |
| `:feature:item` | CRUD de items (producto o servicio según `BusinessType`), gestión de imágenes, formulario |
| `:feature:inventario` | Grid de inventario, diálogos de stock, MVI completo |
| `:feature:profile` | Pantalla de perfil, logout |
| `:feature:history` | Historial de movimientos: filtros de fecha/tipo, paginación por cursor, swipe editar/eliminar |
