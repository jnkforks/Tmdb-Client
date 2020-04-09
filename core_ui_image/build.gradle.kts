plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Deps.Android.Build.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Deps.Android.Build.minSdkVersion)
        targetSdkVersion(Deps.Android.Build.targetSdkVersion)

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    implementation(project(Modules.Core.core))
    implementation(project(Modules.Core.ui))

    implementation(Deps.Glide.core)
    implementation(Deps.Kotlin.std)

    testImplementation(Deps.Test.junit)

    androidTestImplementation(Deps.Test.AndroidX.runner)
    androidTestImplementation(Deps.Test.AndroidX.espresso)
}