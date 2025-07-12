# Mejores Prácticas de Código para MiChambita

Estas directrices tienen como objetivo promover la claridad, mantenibilidad y robustez en la base de código de "MiChambita".

## 1. Convenciones del Lenguaje Kotlin

*   **Sigue las Convenciones Oficiales de Código de Kotlin:** Comienza con la [guía de estilo oficial de Kotlin](https://kotlinlang.org/docs/coding-conventions.html). Esto cubre nombres, formato y uso de características del lenguaje.
*   **Inmutabilidad por Defecto:**
    *   Prefiere `val` sobre `var` siempre que sea posible.
    *   Usa colecciones inmutables (`List`, `Set`, `Map`) en lugar de mutables (`MutableList`, etc.) cuando la colección no necesite cambiar después de su creación. Si se necesita modificación, define claramente dónde y por qué.
*   **Seguridad Nula (Null Safety):**
    *   Aprovecha las características de seguridad nula de Kotlin. Evita `!!` (aserciones no nulas) tanto como sea posible. Prefiere llamadas seguras (`?.`), el operador Elvis (`?:`), o `let`/`run` para manejar tipos nulables.
    *   Define claramente si un tipo debe ser nulable o no nulable.
*   **Funciones de Alcance (`let`, `run`, `with`, `apply`, `also`):**
    *   Úsalas con criterio para mejorar la legibilidad y concisión. Comprende las diferencias entre ellas (valor de retorno, `this` vs. `it`).
    *   No las uses en exceso ni las anides demasiado, ya que puede reducir la legibilidad.
*   **Lambdas y Funciones de Orden Superior:**
    *   Mantén las lambdas cortas y enfocadas.
    *   Si una lambda es compleja o se reutiliza, considera extraerla a una función separada.
*   **Funciones de Extensión:** Úsalas para mejorar clases existentes con funciones de utilidad, pero evita contaminar el espacio de nombres global. Mantenlas relevantes para la clase que extienden.
*   **Data Classes:** Usa `data class` para clases que principalmente almacenan datos. Proveen funciones generadas útiles como `equals()`, `hashCode()`, `toString()` y `copy()`.
*   **Sealed Classes/Interfaces:** Ideales para representar jerarquías restringidas, como estados en tus ViewModels o diferentes tipos de eventos de UI. (¡Ya estás usando esto para Navegación, lo cual es genial!)

## 2. UI con Jetpack Compose

*   **Funciones Composable:**
    *   **Responsabilidad Única:** Cada composable idealmente debería hacer una cosa bien.
    *   **Stateless vs. Stateful:** Prefiere crear composables sin estado que reciban el estado a través de parámetros y eleva el estado a composables de nivel superior (a menudo el composable a nivel de pantalla o el ViewModel).
    *   **Reusabilidad:** Diseña composables para que sean reutilizables.
    *   **Previsualizable:** Usa anotaciones `@Preview` extensivamente para iterar rápidamente en componentes de UI sin necesidad de ejecutar la app en un dispositivo/emulador. Crea vistas previas para diferentes estados y temas.
    *   **Nombres:** Las funciones Composable deben usar PascalCase y típicamente ser sustantivos o frases nominales (ej., `UserProfileCard`, `SubmitButton`).
*   **Gestión de Estado:**
    *   **Flujo de Datos Unidireccional (UDF):** Adhiérete a los principios UDF donde el estado fluye hacia abajo y los eventos fluyen hacia arriba.
    *   **`StateFlow` y `collectAsStateWithLifecycle()`:** Usa `StateFlow` en ViewModels para exponer el estado de la UI y recógelo en Composables usando `collectAsStateWithLifecycle()` para ser consciente del ciclo de vida.
    *   **`remember` y `mutableStateOf`:** Usa `remember` para el estado que es local a un composable y necesita sobrevivir a la recomposición.
    *   **Efectos Secundarios:** Usa `LaunchedEffect`, `DisposableEffect`, `SideEffect` apropiadamente para gestionar efectos secundarios ligados al ciclo de vida del composable.
*   **Modificadores:**
    *   Encadena los modificadores lógicamente.
    *   Haz de los modificadores un parámetro de tus composables reutilizables (`modifier: Modifier = Modifier`) para permitir la personalización desde el lugar de la llamada.
*   **Temas (`TuNegocioTheme.kt`):**
    *   Usa consistentemente `MaterialTheme.colorScheme`, `MaterialTheme.typography`, `MaterialTheme.shapes` en tus composables.
    *   Evita codificar colores, dimensiones o fuentes directamente en los composables. Defínelos en tu tema.

## 3. Arquitectura MVVM

*   **ViewModel (`androidx.lifecycle.ViewModel`):**
    *   **Responsabilidades:** Preparar y gestionar datos para la UI. Manejar interacciones del usuario y lógica de negocio (o delegar a UseCases/Repositorios). Exponer el estado de la UI vía `StateFlow`.
    *   **Sin Dependencias del Framework de Android (Idealmente):** Los ViewModels idealmente no deberían tener referencias a `Context`, `View`, o cualquier clase del framework de Android que tenga un ciclo de vida ligado a la UI. Si se necesita `Context` (ej., para recursos de strings), inyecta el contexto de `Application` vía Hilt.
    *   **Ciclo de Vida:** Los ViewModels sobreviven a cambios de configuración. Usa `viewModelScope` para coroutines que deban estar ligadas al ciclo de vida del ViewModel.
*   **Repositorio:**
    *   **Única Fuente de Verdad:** Los repositorios deben ser la única fuente de verdad para los datos. Abstraen las fuentes de datos (red, base de datos local).
    *   **Dirigido por Interfaces:** Define interfaces de repositorio y provee implementaciones concretas. Esto ayuda con las pruebas y la flexibilidad.
    *   **Funciones Suspend:** Las operaciones de datos (llamadas de red, acceso a base de datos) deben ser funciones `suspend` para ser llamadas desde coroutines.
*   **UseCase (Opcional pero Recomendado para Lógica Compleja):**
    *   Encapsula piezas específicas de lógica de negocio.
    *   Puede orquestar llamadas a uno o más repositorios.
    *   Promueve la separación de responsabilidades y la testeabilidad.
    *   Típicamente, una clase con una única función pública operadora `invoke`.

## 4. Base de Datos Room

*   **DAOs (Data Access Objects):**
    *   Define los DAOs como interfaces.
    *   Usa funciones `suspend` para operaciones de una sola vez (insertar, actualizar, eliminar, consulta simple).
    *   Usa `Flow<T>` para consultas observables que se actualizan automáticamente cuando los datos subyacentes cambian.
*   **Entidades:**
    *   Mantén las entidades como simples contenedores de datos.
    *   Usa las anotaciones apropiadas (`@Entity`, `@PrimaryKey`, `@ColumnInfo`, `@Embedded`, `@Relation`).
*   **Migraciones de Base de Datos:** Planifica los cambios de esquema de la base de datos proveyendo estrategias de `Migration` adecuadas. Pruébalas exhaustivamente.
*   **Hilos (Threading):** Room asegura que las operaciones de base de datos no se realicen en el hilo principal por defecto cuando se usan funciones `suspend` o `Flow`.

## 5. Hilt para Inyección de Dependencias

*   **Anotar Puntos de Entrada:** Usa `@AndroidEntryPoint` para Activities, Fragments, Services, etc.
*   **Inyección por Constructor:** Prefiere la inyección por constructor siempre que sea posible.
*   **Módulos (`@Module`, `@InstallIn`):** Usa módulos Hilt para proveer dependencias para tipos que no posees (ej., interfaces, clases de librerías externas como Retrofit o instancias de base de datos Room).
*   **Alcances (Scopes):** Usa los alcances Hilt apropiados (`@Singleton`, `@ActivityScoped`, `@ViewModelScoped`) para gestionar el ciclo de vida de tus dependencias.

## 6. Coroutines y Operaciones Asíncronas

*   **Concurrencia Estructurada:** Lanza coroutines en alcances apropiados (`viewModelScope`, `lifecycleScope`, o alcances personalizados). Evita `GlobalScope`.
*   **Dispatchers:** Usa los dispatchers apropiados:
    *   `Dispatchers.Main`: Para actualizaciones de UI.
    *   `Dispatchers.IO`: Para operaciones de red y disco.
    *   `Dispatchers.Default`: Para trabajo intensivo de CPU.
*   **Manejo de Errores:** Usa bloques `try-catch` dentro de las coroutines o `CoroutineExceptionHandler` para un manejo de errores robusto.
*   **Cancelación:** Asegura que las coroutines sean cancelables y respondan a la cancelación.

## 7. Offline First y Sincronización (`SyncManager`, `NetworkManager`)

*   **Única Fuente de Verdad (Local):** La base de datos Room debe ser la fuente primaria de verdad. La UI lee de Room.
*   **Lógica de Sincronización:**
    *   Mantén la lógica de sincronización robusta y testeable.
    *   Maneja conflictos (ej., la última escritura gana usando tu marca de tiempo `lastUpdated`).
    *   Gestiona estados de sincronización (inactivo, sincronizando, error) y provee retroalimentación al usuario.
*   **Conciencia de Red:** Reacciona apropiadamente a los cambios de conectividad de red. Encola los cambios hechos offline para sincronizar cuando la red esté disponible.

## 8. Gestión de Recursos

*   **Recursos de Strings:** Usa `strings.xml` para todo el texto visible por el usuario para localización y mantenibilidad.
*   **Recursos de Dimensiones:** Usa `dimens.xml` para estandarizar paddings, márgenes, tamaños de texto.
*   **Recursos de Color:** Define la paleta de colores de tu app en `colors.xml` (aunque con Material 3 y Compose, principalmente los definirás en tu `Theme.kt`).

## 9. Pruebas (Testing)

*   **Pruebas Unitarias:**
    *   ViewModels (probar cambios de estado, lógica).
    *   Repositorios (probar manipulación de datos, interacción con fuentes de datos - a menudo mockeadas).
    *   UseCases.
    *   Clases de ayuda/utilidades.
*   **Pruebas de UI (Compose):**
    *   Usa `ComposeTestRule` para probar composables individuales y flujos de pantalla.
    *   Verifica elementos de UI, interacciones y cambios de estado.
*   **Pruebas de Integración:** Prueba interacciones entre diferentes componentes (ej., ViewModel -> Repositorio -> DAO).

## 10. Calidad General del Código

*   **Legibilidad:** Escribe código que sea fácil de entender para otros (y para tu yo futuro).
    *   Nombres claros de variables y funciones.
    *   Formato consistente (usa el auto-formateador de Android Studio).
    *   Añade comentarios donde sea necesario para explicar lógica compleja o decisiones no obvias. Evita comentar en exceso código simple.
*   **DRY (Don't Repeat Yourself - No te Repitas):** Evita duplicar código. Extrae la lógica común en funciones o clases reutilizables.
*   **Principios SOLID:** Ten en mente los principios SOLID para un diseño orientado a objetos mantenible y flexible.
*   **Manejo de Errores:** Implementa un manejo de errores robusto. No solo captures excepciones; manéjalas con gracia e informa al usuario si es necesario.
*   **Logging:** Usa logging apropiado (`Log.d`, `Log.i`, `Log.e`) para depuración, pero ten cuidado de lanzar apps con logging excesivo. Considera usar una librería de logging como Timber.

---

Al adoptar estas prácticas, construirás una aplicación "MiChambita" más robusta, escalable y mantenible. Es un proceso continuo, ¡así que revisa y refina tu enfoque regularmente!
