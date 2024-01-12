plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.cookbook.app"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cookbook.app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        viewBinding = true
        compose = true
    }
    dataBinding{
        enable = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "33.0.0"
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.google.code.gson:gson:2.8.8") // Use the latest version available
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //HILT
    implementation("com.google.dagger:hilt-android:2.44")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    //Data Binding
    // Data binding
    kapt("com.android.databinding:compiler:3.2.0-alpha10")
    kapt("androidx.databinding:databinding-common:4.0.2")
    //Image coil
    implementation("io.coil-kt:coil:1.0.0")
    // Recyclerview
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    // Room components
    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")
    androidTestImplementation("androidx.room:room-testing:2.2.5")

    //DS
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha01")
    //
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("org.jsoup:jsoup:1.11.3")

    //Admob
    implementation("com.google.android.gms:play-services-ads:22.6.0")

}
kapt {
    javacOptions {
      //  option("-source", "1.8")
    //    option("-target", "1.8")
    }
}