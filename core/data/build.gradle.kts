plugins {
    id("michambita.android.library")
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.michambita.core.data"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    // DataStore
    implementation(libs.androidx.datastore)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

    // ThreeTenABP
    implementation(libs.threetenabp)

    // Compose (needed for ImageModule)
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
}
