# 📦 DEPENDENCIES.md — Proyecto MiChambita

Este archivo documenta todas las dependencias y plugins utilizados en el proyecto **MiChambita**, incluyendo sus versiones, propósitos y recomendaciones.

---

## ⚙️ Versiones del entorno

| Herramienta                    | Versión    |
|-------------------------------|------------|
| Kotlin                        | 1.9.23     |
| Gradle Wrapper                | 8.7        |
| Android Gradle Plugin (AGP)   | 8.4.1      |
| Compose BOM                   | 2024.03.00 |
| Java                          | 17         |
| Min SDK                       | 23         |
| Target SDK                    | 34         |

---

## 🧩 Plugins

| Plugin                         | ID                                 | Versión   |
|--------------------------------|-------------------------------------|-----------|
| Android Application Plugin     | `com.android.application`          | 8.1.1     |
| Kotlin Android Plugin          | `org.jetbrains.kotlin.android`     | 1.9.23    |
| Kotlin Kapt                   | `org.jetbrains.kotlin.kapt`        | 1.9.23    |
| Hilt Plugin                    | `com.google.dagger.hilt.android`   | 2.48      |

---

## 📚 Dependencias principales

### 🔷 Jetpack Compose

| Librería                    | Versión       | Descripción                         |
|----------------------------|----------------|-------------------------------------|
| Compose BOM                | 2024.03.00     | Manejo centralizado de versiones   |
| Material 3                 | 1.1.2          | Componentes UI modernos             |
| Activity Compose           | 1.8.2          | Integración de Compose en Activity |
| Navigation Compose         | 2.7.6          | Navegación entre pantallas          |

> ✅ *Se utiliza `platform()` para manejar el Compose BOM en el archivo Gradle.*

---

### 🧱 AndroidX Core

| Librería                     | Versión   | Descripción                        |
|-----------------------------|-----------|------------------------------------|
| core-ktx                    | 1.10.1    | Extensiones Kotlin para Android    |
| lifecycle-runtime-ktx      | 2.6.2     | Lifecycle-aware coroutines         |

---

### 🗃️ Room (Persistencia local)

| Librería             | Versión   | Uso                             |
|----------------------|-----------|----------------------------------|
| room-runtime         | 2.6.1     | Implementación principal         |
| room-ktx             | 2.6.1     | Extensiones Kotlin               |
| room-compiler        | 2.6.1     | Generación de código (usado con kapt) |

---

### ⚙️ DataStore

| Librería              | Versión   | Descripción                   |
|-----------------------|-----------|-------------------------------|
| datastore-preferences | 1.1.0     | Reemplazo moderno de SharedPreferences |

---

### 🔐 Hilt (Inyección de dependencias)

| Librería                  | Versión   | Uso                             |
|---------------------------|-----------|----------------------------------|
| hilt-android              | 2.48      | Inyección de dependencias       |
| hilt-android-compiler     | 2.48      | Generación de código (usado con kapt) |

---

### 🔥 Firebase

| Librería                  | Versión    | Uso                           |
|---------------------------|------------|--------------------------------|
| firebase-auth-ktx         | 22.3.1     | Login con email o celular      |
| firebase-firestore-ktx    | 24.10.3    | Base de datos en la nube       |

---

### 🌐 Networking (preparado para backend propio)

| Librería         | Versión   | Uso                             |
|------------------|-----------|----------------------------------|
| retrofit         | 2.9.0     | Cliente HTTP                     |
| converter-gson   | 2.9.0     | Conversión JSON <-> objetos      |

---

> Última actualización: julio 2025