# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MiChambita is a native Android (Kotlin + Jetpack Compose) app for micro-business owners (informal entrepreneurs without a RUC) to manage items (products or services), inventory, cash movements (sales/expenses), and a movement history. Data flow is currently **online-first via Firestore**; the offline-first infrastructure (Room + WorkManager) is preserved but disabled. See [README.md](README.md) for product-level feature details and [AGENTS.md](AGENTS.md) for the canonical module/dependency-rule reference — keep both in sync with this file when architecture changes.

## Commands

Build system is Gradle with convention plugins in `build-logic/`; there is no separate lint/format tooling configured (no detekt/ktlint/spotless, no CI workflows).

```bash
./gradlew build                          # Build all modules
./gradlew assembleDebug                  # Build debug APK
./gradlew :app:installDebug              # Install debug APK on a connected device/emulator
./gradlew :feature:history:build         # Build a single module (substitute module path)
./gradlew test                           # Run all JVM unit tests
./gradlew :app:testDebugUnitTest         # Run unit tests for one module
./gradlew connectedAndroidTest           # Run instrumented tests (needs device/emulator)
./gradlew lint                           # Run Android Lint (the only static analysis available)
```

Only placeholder template tests exist (`app/src/test/.../ExampleUnitTest.kt`, `app/src/androidTest/.../ExampleInstrumentedTest.kt`) — there is no real test suite to run yet.

## Architecture

### Module graph and dependency rules

```
:app                          ← Entry point, @HiltAndroidApp, Google Services, WorkManager init
  └─ :router                  ← NavigationGraph, MainContainer (bottom-level NavHost + top bar), MainViewModel
       └─ :feature:*          ← Feature modules (UI)
            └─ :ui            ← Theme (Color, Type, Shape), reusable Compose components
            └─ :domain        ← Business models, repository interfaces, use cases
                 └─ :common   ← UiState, Screen (routes), DateUtils, NetworkState
  └─ :data                    ← Room, Firebase, DataStore, Retrofit, Workers
       └─ :domain, :common
```

Enforced dependency direction (do not violate when adding code):

| From | May depend on | Must NOT depend on |
|------|----------------|---------------------|
| `:app` | everything | — |
| `:router` | `:feature:*`, `:ui`, `:domain`, `:common` | `:data` |
| `:feature:*` | `:ui`, `:domain`, `:common` | `:data`, `:router`, other `:feature:*` modules |
| `:ui` | `:domain` | `:data`, `:common`, `:feature:*` |
| `:data` | `:domain`, `:common` | `:ui`, `:feature:*`, `:router` |
| `:domain` | `:common` | everything else |
| `:common` | nothing (leaf module) | everything |

Feature modules never depend on each other directly; anything shared between features belongs in `:ui`, `:domain`, or `:common`. `:router` is the only module aware of all features simultaneously — it wires them into navigation.

Current feature modules: `:feature:auth`, `:feature:home`, `:feature:item` (product CRUD — referred to as "producto" in product docs but the module/package is `item`), `:feature:inventario`, `:feature:profile`, `:feature:history`. Note `:app/build.gradle.kts` currently only wires up `auth`, `home`, `item`, and `inventario` as direct dependencies — `profile` and `history` are pulled in transitively via `:router` (which does depend on them) and are actually rendered inside `MainContainer`'s nested `NavHost`, not `NavigationGraph`'s top-level one.

### Convention plugins (`build-logic/convention/`)

All modules must use these instead of configuring `compileSdk`/`minSdk`/`composeOptions` directly:

| Plugin ID | Configures |
|-----------|------------|
| `michambita.android.application` | `com.android.application` + Kotlin + Hilt + Compose, compileSdk/minSdk/targetSdk 34/23/34, Java 17, test deps |
| `michambita.android.library` | `com.android.library` + Kotlin, compileSdk 34, minSdk 23, Java 17, `buildConfig = false` |
| `michambita.android.hilt` | kapt + Dagger Hilt 2.48 |
| `michambita.android.compose` | Compose BOM, Material 3, Navigation Compose, Hilt Navigation, Lifecycle, debug tooling |

All external dependency versions live in `gradle/libs.versions.toml` (version catalog) — never hardcode a dependency version inline in a module's `build.gradle.kts`.

### Two-tier navigation

`Screen` (sealed class in `:common`) centralizes every route. Navigation happens in two nested layers:
- `NavigationGraph` (`:router`) — top-level `NavHost`: Splash → Login/Registro → `MainContainer`, gated by `SessionViewModel`'s auth + business-type state.
- `MainContainer` (`:router`) — owns its own `NavHost` for the authenticated app shell (Home, Item, Inventario, Profile, History) plus the shared `TopAppBar`.

Global connectivity: `NavigationGraph` overlays a non-dismissable `NoConnectionModal` (`:ui`) whenever `SessionViewModel.isOnline` is `false`. That flag is backed by the `NetworkState` contract (interface in `:common/network`, impl `NetworkStateImpl` in `:data` tracking validated networks — `NET_CAPABILITY_INTERNET` + `NET_CAPABILITY_VALIDATED` — to avoid false offline flashes). Firestore's persistent cache still serves data behind the modal; Home is intentionally **not** forced into a loading state while offline.

### State management pattern

- **`UiState<T>`** (sealed class in `:common`): `Empty | Loading | Success<T> | Error(message)` — the single state pattern across the app. ViewModels extend `ViewModel()`, expose a `StateFlow` of a bespoke `*UiState` data class backed by a private `MutableStateFlow` (updated via `_uiState.update { it.copy(...) }`), and expose plain public functions for user actions (Home, Item, Profile, History, Inventario all follow this shape). Don't introduce MVI/Intent-Effect or other state frameworks.

### Dependency injection

Hilt via kapt (not KSP) throughout. `@HiltAndroidApp` on the `:app` Application class, `@AndroidEntryPoint` on Activities, `@HiltViewModel` + `@Inject constructor` on ViewModels. Provider modules (`RepositoryModule`, `FirebaseModule`, etc.) live in `:data`. Cross-cutting contracts that aren't repositories get their own module rather than being lumped into `RepositoryModule` — e.g. `NetworkState` is bound by a dedicated `NetworkModule` in `:data`.

### Data flow: online-first, with dormant offline infrastructure

The active flow is direct Firestore CRUD: movements live under `companies/{companyId}/movements` and go through the `*OnlineUseCase`s (`AddMovimientoOnlineUseCase`, `UpdateMovimientoOnlineUseCase`, `DeleteMovimientoOnlineUseCase`); Home observes today's movements via a snapshot listener (`GetMovimientosOnlineUseCase`), and History queries by date range with cursor pagination (`MovimientoRepository.getMovimientosHistorial`). User/company preferences (including business type) persist in DataStore (`data_preferences`).

An offline-first path is preserved but **disabled**: Room (`SynchronizationDB`, `MovimientoEntity`) plus `SyncMovimientosWorker` (nightly WorkManager sync, 0–5 h window) exist, but `setupWorkManager()` is commented out in `MiChambitaApp` and the offline use cases are unused. Don't document or extend it as the active flow unless it gets re-enabled.

### Naming conventions

- Core modules: flat names (`:common`, `:domain`, `:data`, `:ui`, `:router`), package `com.michambita.core.<module>`.
- Feature modules: `:feature:<name>`, package `com.michambita.feature.<name>`.
