import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("michambita.android.library")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.11"
                }
            }

            val libs = extensions.getByType(
                org.gradle.api.artifacts.VersionCatalogsExtension::class.java
            ).named("libs")

            dependencies {
                // Compose BOM
                add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("implementation", libs.findLibrary("androidx.material.icons.extended").get())

                // Compose UI
                add("implementation", "androidx.compose.ui:ui")
                add("implementation", "androidx.compose.ui:ui-tooling-preview")
                add("implementation", "androidx.compose.ui:ui-text")
                add("implementation", "androidx.compose.material3:material3")

                // Navigation + Hilt Navigation
                add("implementation", "androidx.navigation:navigation-compose")
                add("implementation", "androidx.hilt:hilt-navigation-compose:1.1.0")

                // Lifecycle
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose")

                // Activity Compose
                add("implementation", "androidx.activity:activity-compose")

                // Debug tooling
                add("debugImplementation", "androidx.compose.ui:ui-tooling")
            }
        }
    }
}
