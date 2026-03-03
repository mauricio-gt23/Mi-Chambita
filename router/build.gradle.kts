plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.router"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:producto"))
    implementation(project(":feature:inventario"))
}
