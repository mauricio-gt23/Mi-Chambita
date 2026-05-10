plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.feature.auth"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":ui"))
}
