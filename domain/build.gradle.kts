plugins {
    id("michambita.android.library")
}

android {
    namespace = "com.michambita.domain"
}

dependencies {
    implementation(project(":common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
