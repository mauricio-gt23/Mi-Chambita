# 📱 MiChambita

**MiChambita** es una aplicación Android desarrollada en Kotlin, pensada para **microempresarios informales y emprendedores jóvenes (sin RUC)**. Brinda una herramienta sencilla y moderna para **registrar ventas, gastos e inventario**, con modo offline y sincronización automática con la nube.

---

## 🚀 Funcionalidades principales

- ✅ Registro de ventas offline (monto + descripción)
- ✅ Registro de gastos por categoría, monto y fecha
- ✅ Gestión de inventario: producto, cantidad y costo
- ✅ Autenticación sin contraseña (Firebase Auth con email link o número celular)
- ✅ Resumen diario/semanal con gráficos o tablas
- ✅ Onboarding opcional al primer inicio
- ✅ Sincronización automática con Firestore cuando hay conexión
- ✅ Indicadores visuales de estado de red y sincronización

---

## 🛠️ Tecnologías y arquitectura

- **Lenguaje:** Kotlin
- **Arquitectura:** MVVM (ViewModel + StateFlow)
- **UI:** Jetpack Compose + Material 3 (Material You)
- **Persistencia local:**
    - Room (con relaciones entre entidades)
    - DataStore (para configuración y preferencias)
- **Inyección de dependencias:** Hilt
- **Backend:**
    - Firebase Authentication
    - Firebase Firestore (modo offline activado)
- **Preparado para migración a:** Spring Boot (vía Retrofit)
- **Mínimo API:** 23 (Android 6.0)

---

## 🎨 Diseño y UI

- Material 3 (Material You) + Dynamic Colors
- Tema claro/oscuro basado en `isSystemInDarkTheme()`
- Archivo `TuNegocioTheme.kt` define:
    - Paleta de colores (`primary`, `secondary`, `background`)
    - Tipografía personalizada (`Typography`)
    - Formas (`Shapes`)

---

## 🧭 Navegación

- `Navigation-Compose` con rutas definidas mediante `sealed class Screen`
- Paso de parámetros entre pantallas
- Soporte para backstack y restauración de estado

---

## 📂 Estructura del proyecto
MiChambita/
│
├── data/
│ ├── local/
│ │ ├── database/
│ │ ├── dao/
│ │ └── entity/
│ ├── remote/
│ └── repository/
│
├── domain/
│ ├── model/
│ └── usecase/
│
├── ui/
│ ├── screen/
│ │ ├── login/
│ │ ├── ventas/
│ │ ├── gastos/
│ │ ├── inventario/
│ │ └── resumen/
│ ├── components/
│ └── theme/
│
├── utils/
├── di/
├── navigation/
└── build.gradle.kts

---

## 🔄 Modo offline y sincronización automática

- Arquitectura **offline-first**: todos los datos se guardan primero en Room
- Firestore con `FirebaseFirestoreSettings(persistenceEnabled = true)`
- Sistema de sincronización automática:
    - Detecta conexión a internet
    - Compara cambios usando `lastUpdated: Long`
    - Actualiza datos locales/remotos según corresponda
- Indicadores visuales: `Snackbar` o íconos de sincronización
- `NetworkManager` implementado con `ConnectivityManager` y `callbackFlow`
- `SyncManager` escucha cambios en la red y dispara sincronización

---

## 📤 Funcionalidades premium (futuras versiones)

- Backup automático completo en Firestore
- Exportación de reportes mensuales en PDF o Excel
- Reportes por categoría o producto
- Control de ventas fiadas y clientes deudores

---

## 🧪 Testing

- Pruebas unitarias: ViewModel, UseCases y Repositories
- Pruebas de UI: Jetpack Compose con `ComposeTestRule`
- Simulación de cambios de red y validación del sistema de sincronización

---

## 🔐 Seguridad

- `FLAG_SECURE` en actividades sensibles
- Validación de entradas de usuario (ej: monto, nombres)
- (Futuro) Encriptación de backups locales

---

## 🧪 Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/MiChambita.git