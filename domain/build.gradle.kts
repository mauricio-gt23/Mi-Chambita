plugins {
    id("michambita.android.library")
    id("michambita.android.hilt")
}

android {
    namespace = "com.michambita.domain"
}

dependencies {
    implementation(project(":common"))
}
