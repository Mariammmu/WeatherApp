plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.1.10"

    id("kotlin-parcelize")


}

android {
    namespace = "com.mariammuhammad.climate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mariammuhammad.climate"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //Lottie
    implementation("com.airbnb.android:lottie-compose:4.0.0")
    //Scoped API
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.12.1")
    implementation(libs.play.services.location)


//okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //splashScreen
    implementation ("androidx.core:core-splashscreen:1.0.0")
    implementation(libs.androidx.work.runtime.ktx)

    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    // Kotlin Symbol Processing (KSP)
    ksp ("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    //Glide
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")
    //LiveData & Compose
    val compose_version = "1.0.0"
    implementation ("androidx.compose.runtime:runtime-livedata:$compose_version")

    val nav_version = "2.8.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")
//Serialization for NavArgs
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // Lifecycle dependencies
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")


    // Google Maps SDK for Android
    implementation("com.google.maps.android:maps-compose:6.5.2")
   implementation("com.google.android.libraries.places:places:3.3.0")

    //Testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    //MockK
    testImplementation ("io.mockk:mockk-android:1.13.17")
    testImplementation ("io.mockk:mockk-agent:1.13.17")

    //kotlinx-coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")


    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    // AndroidX and Robolectric
    testImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation ("androidx.test:core-ktx:1.5.0")
    testImplementation ("org.robolectric:robolectric:4.11")

    // hamcrest
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")

    // AndroidX Test - Instrumented testing
    androidTestImplementation ("androidx.test:core:1.6.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    // AndroidX Test - JVM testing
    testImplementation ("androidx.test:core-ktx:1.6.1")

    // Dependencies for local unit tests
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.robolectric:robolectric:4.12")

//    implementation ("com.google.maps.android:maps-compose:4.3.0")
//    implementation ("com.google.android.gms:play-services-maps:19.1.0")
//    implementation ("com.google.android.gms:play-services-location:21.3.0")
//    implementation ("com.google.android.libraries.places:places:4.2.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}