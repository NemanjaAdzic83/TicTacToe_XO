plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tictactoe_xo"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.tictactoe_xo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation(libs.material)
    implementation ("com.google.android.material:material:1.11.0")

    implementation(libs.activity)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")

    implementation(libs.constraintlayout)
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.legacy.support.v4)

    testImplementation(libs.junit)
    testImplementation ("junit:junit:4.13.2")

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
}