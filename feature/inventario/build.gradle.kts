plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.feature.inventario"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
}
