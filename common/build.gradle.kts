plugins {
    id("michambita.android.library")
}

android {
    namespace = "com.michambita.common"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}
