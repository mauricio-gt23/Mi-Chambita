plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.ui"
}

dependencies {
    implementation(project(":domain"))
    api(libs.coil)
    api(libs.coil.compose)
}
