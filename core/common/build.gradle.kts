plugins {
    id("michambita.android.feature")
}

android {
    namespace = "com.michambita.core.common"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")
}
