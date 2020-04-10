plugins {
    id("com.gradle.enterprise") version "3.1.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

include(
    ":app",
    ":service_tmdb",
    ":core",
    ":service_analytics",
    ":core_storage",
    ":core_ui",
    ":core_ui_image",
    ":core_test",
    ":app_features",
    ":app_features_settings",
    ":core_tools",
    ":debug"
)