plugins {
    id("michambita.android.library")
    id("michambita.android.hilt")
    id("michambita.android.compose")
}

android {
    namespace = "com.michambita.router"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":ui"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:producto"))
    implementation(project(":feature:inventario"))
    implementation(project(":feature:profile"))
}
