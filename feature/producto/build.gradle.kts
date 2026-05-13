plugins {
    id("michambita.android.library")
    id("michambita.android.hilt")
    id("michambita.android.compose")
}

android {
    namespace = "com.michambita.feature.producto"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":ui"))
}
