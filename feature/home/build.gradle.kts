plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.feature.home"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":ui"))
    implementation(project(":feature:producto"))
    implementation(project(":feature:inventario"))
}
