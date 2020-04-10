import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

android {
  compileSdkVersion(Deps.Android.Build.compileSdkVersion)

  defaultConfig {
    minSdkVersion(Deps.Android.Build.minSdkVersion)
    targetSdkVersion(Deps.Android.Build.targetSdkVersion)

    versionCode = 1
    versionName = "1.0"

    vectorDrawables.useSupportLibrary = true

    testInstrumentationRunner = "com.illiarb.tmdbexplorer.functional.AppRunner"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      isDebuggable = false
      isZipAlignEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  buildFeatures {
    viewBinding = true
  }

  signingConfigs {
    getByName("debug") {
      val propsFile = rootProject.file("keystore.properties")
      if (propsFile.exists()) {
        val props = Properties().also { it.load(FileInputStream(propsFile)) }

        storeFile = file(props.getProperty("storeFile"))
        storePassword = props.getProperty("storePassword")
        keyAlias = props.getProperty("keyAlias")
        keyPassword = props.getProperty("keyPassword")
      }
    }
  }
}

dependencies {

  kapt(Deps.Dagger.compiler)

  implementation(project(Modules.Core.core))
  implementation(project(Modules.Core.ui))
  implementation(project(Modules.Core.uiImage))
  implementation(project(Modules.Core.storage))
  implementation(project(Modules.Core.tools))
  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))
  implementation(project(Modules.appFeatures))
  implementation(project(Modules.featureSettings))
  implementation(project(Modules.debug))

  implementation(Deps.Dagger.core)
  implementation(Deps.Misc.timber)
  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.AndroidX.navigation)

  // Play core lib for downloading dynamic features
  //implementation deps.playCore

  debugImplementation(Deps.Tools.LeakCanary.android)
  debugImplementation(Deps.Tools.LeakCanary.objectWatcher)

  androidTestImplementation(Deps.Test.AndroidX.espressoIntents)
  androidTestImplementation(Deps.Test.AndroidX.runner)
  androidTestImplementation(Deps.Test.AndroidX.uiAutomator)
  androidTestImplementation(Deps.Test.AndroidX.extJunit)
  androidTestImplementation(Deps.Test.kakao)
  androidTestImplementation(Deps.Test.kaspresso)
}

apply {
  plugin("com.google.gms.google-services")
}