plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.feature.auth"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
}
