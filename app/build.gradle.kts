plugins {


        id("com.android.application")  // BEZ version "8.13.2"
        id("org.jetbrains.kotlin.android")  // BEZ version "..."
 
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"  // SADA RADI
    
}

android {
    namespace = "dev.radovanradivojevic.hiit2"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.radovanradivojevic.hiit2"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "2.0"
    }

    buildFeatures {
        compose = true
    }

    // STARI NAČIN za Kotlin 1.9.x:
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear.compose:compose-material:1.3.0")
    implementation("androidx.wear.compose:compose-foundation:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-service:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")

    // GPS Location
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.compose.material3:material3:1.2.0")
        // Dodaj ovo:
        implementation("androidx.core:core-splashscreen:1.0.1")

        // Ostali dependency-i ostaju isti...
        implementation("androidx.core:core-ktx:1.12.0")
        // ... itd.

}