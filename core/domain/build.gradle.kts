plugins {
    id("michambita.android.library")
}

android {
    namespace = "com.michambita.core.domain"
}

dependencies {
    implementation(project(":core:common"))
}
