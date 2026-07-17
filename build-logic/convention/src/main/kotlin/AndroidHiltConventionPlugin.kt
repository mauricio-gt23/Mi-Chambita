import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.kapt")
            pluginManager.apply("com.google.dagger.hilt.android")

            dependencies {
                add("implementation", libs.findLibrary("dagger.hilt.android").get())
                add("kapt", libs.findLibrary("dagger.hilt.compiler").get())
                
                // Note: When migrating to KSP, replace kapt with ksp plugin and dependency configurations here.
                // e.g., pluginManager.apply("com.google.devtools.ksp")
                // add("ksp", .libs.findLibrary("dagger.hilt.compiler").get())
            }

        }
    }
}
