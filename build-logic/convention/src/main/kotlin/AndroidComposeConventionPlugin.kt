import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val composeCompilerVersion = libs.findVersion("composeCompiler").get().toString()

            // Configura Compose cuando el módulo es tipo Application
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    buildFeatures { compose = true }
                    composeOptions { kotlinCompilerExtensionVersion = composeCompilerVersion }
                }
            }

            // Configura Compose cuando el módulo es tipo Library
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures { compose = true }
                    composeOptions { kotlinCompilerExtensionVersion = composeCompilerVersion }
                }
            }

            dependencies {
                // Compose BOM — gestiona versiones de todo el stack Compose
                add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("implementation", libs.findLibrary("androidx.material.icons.extended").get())

                // Compose UI
                add("implementation", "androidx.compose.ui:ui")
                add("implementation", "androidx.compose.ui:ui-tooling-preview")
                add("implementation", "androidx.compose.material3:material3")

                // Navigation + Hilt Navigation Compose
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())

                // Lifecycle
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose")

                // Debug tooling (solo visible en builds debug)
                add("debugImplementation", "androidx.compose.ui:ui-tooling")
            }
        }
    }
}
