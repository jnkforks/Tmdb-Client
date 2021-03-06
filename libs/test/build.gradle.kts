plugins {
  id("com.android.library")
  kotlin("android")
}

apply {
  from(rootProject.file("gradle/configure-kotlin-sources.gradle"))
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

  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))
  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.tools))

  debugImplementation(Deps.Tools.Debug.Flipper.flipper)
  debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)

  api(Deps.Kotlin.reflect)
  api(Deps.Kotlin.coroutinesTest)
  api(Deps.Test.AndroidX.core)
  api(Deps.Test.AndroidX.rules)
  api(Deps.Test.AndroidX.extJunit)
  api(Deps.Test.JUnit5.jupiterApi)
  api(Deps.Test.JUnit5.jupiterParams)
  api(Deps.Test.truth)
  api(Deps.Test.mockk)
  api(Deps.Test.junit)
}