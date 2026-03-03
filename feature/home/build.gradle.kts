plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.feature.home"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":feature:producto"))
    implementation(project(":feature:inventario"))
}
