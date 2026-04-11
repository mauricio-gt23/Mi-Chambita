plugins {
    id("michambita.android.application")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.michambita"

    defaultConfig {
        applicationId = "com.michambita"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Module dependencies
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":router"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:producto"))
    implementation(project(":feature:inventario"))

    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

    // ThreeTenABP
    implementation(libs.threetenabp)

    // Firebase
    implementation(libs.firebase.auth)
}
