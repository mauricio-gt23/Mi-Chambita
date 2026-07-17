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
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":ui"))
    implementation(project(":router"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:item"))
    implementation(project(":feature:inventario"))

    // WorkManager: solo runtime
    implementation(libs.androidx.work.runtime)

    // ThreeTenABP
    implementation(libs.threetenabp)

    // Firebase
    implementation(libs.firebase.auth)
}
