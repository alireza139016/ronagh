plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zanganeh.ronagh"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zanganeh.ronagh"
        minSdk = 23
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
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
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    // Cafe Bazaar in-app billing (Poolakey). Use the latest release from github.com/PHELAT/Poolakey/releases
    implementation("com.github.cafebazaar.Poolakey:poolakey:2.1.0")
}
