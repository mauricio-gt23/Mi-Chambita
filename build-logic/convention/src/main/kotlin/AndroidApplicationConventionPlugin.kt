import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("michambita.android.hilt")
            pluginManager.apply("michambita.android.compose")

            extensions.configure<ApplicationExtension> {
                compileSdk = AndroidSdk.COMPILE

                defaultConfig {
                    minSdk = AndroidSdk.MIN
                    targetSdk = AndroidSdk.TARGET
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                compileOptions { configureJava17() }

                packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
            }

            dependencies {
                // Activity Compose — exclusivo de app (único módulo con Activity real)
                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", libs.findLibrary("androidx.activity").get())

                // Testing
                add("testImplementation", libs.findLibrary("junit4").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.ext.junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.espresso.core").get())
                add("androidTestImplementation", "androidx.compose.ui:ui-test-junit4")
                add("debugImplementation", "androidx.compose.ui:ui-test-manifest")
            }

            configureKotlinJvmTarget()
        }
    }
}
