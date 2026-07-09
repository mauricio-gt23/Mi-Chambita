plugins {
    id("michambita.android.library")
    id("michambita.android.hilt")
    id("michambita.android.compose")
}

android {
    namespace = "com.michambita.ui"
}

dependencies {
    implementation(project(":domain"))
    api(libs.coil)
    api(libs.coil.compose)
}
