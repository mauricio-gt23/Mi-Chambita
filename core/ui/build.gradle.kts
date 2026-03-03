plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    api(libs.coil)
    api(libs.coil.compose)
}
