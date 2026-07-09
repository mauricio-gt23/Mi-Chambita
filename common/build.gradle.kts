plugins {
    id("michambita.android.library")
}

android {
    namespace = "com.michambita.common"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
}
