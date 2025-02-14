plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // RetroFit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // Room dependencies (updated to the latest version)
    implementation("androidx.room:room-runtime:2.6.1") // Latest Room runtime
    kapt("androidx.room:room-compiler:2.6.1") // Annotation processor for Room
    implementation("androidx.room:room-ktx:2.6.1") // Room with coroutine support

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Lifecycle and ViewModel components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // Latest version of ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Latest version of coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")

    //gps
    implementation("com.google.android.gms:play-services-location:21.3.0")

    //splashscreen
    implementation("androidx.core:core-splashscreen:1.0.0")
}