plugins {
    id("michambita.android.library")
    id("michambita.android.hilt")
    id("michambita.android.compose")
}

android {
    namespace = "com.michambita.feature.history"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":ui"))
}
