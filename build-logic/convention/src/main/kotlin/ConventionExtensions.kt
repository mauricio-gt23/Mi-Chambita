import com.android.build.api.dsl.CompileOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Helpers compartidos por los convention plugins.
 * */

/** Niveles de SDK compartidos por todos los módulos Android. */
internal object AndroidSdk {
    const val COMPILE = 34
    const val MIN = 23
    const val TARGET = 34
}

/** Acceso al version catalog `.libs` desde un convention plugin. */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/** Java 17 para módulos Android (receiver = bloque `compileOptions {}`). */
internal fun CompileOptions.configureJava17() {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

/** Java 17 para módulos Kotlin/JVM puros (receiver = extensión `java {}`). */
internal fun JavaPluginExtension.configureJava17() {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

/** jvmTarget 17 para las tareas KotlinCompile. Android y JVM comparten el mismo */
internal fun Project.configureKotlinJvmTarget() {
    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions { jvmTarget = "17" }
    }
}
