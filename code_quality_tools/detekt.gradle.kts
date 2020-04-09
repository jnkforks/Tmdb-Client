import io.gitlab.arturbosch.detekt.detekt

apply {
    plugin("io.gitlab.arturbosch.detekt")
}

detekt {
    failFast = false
    buildUponDefaultConfig = true
    autoCorrect = true
    config = files("$rootDir/code_quality_tools/detekt_config.yml")
    reports {
        html {
            enabled = true
            destination = file("$rootDir/reports/detekt/${project.name}_report.html")
        }
    }
    idea {
        path = "$rootDir/.idea"
        codeStyleScheme = "$rootDir/.idea/idea-code-style.xml"
        inspectionsProfile = "$rootDir/.idea/inspect.xml"
        report = "project.projectDir/reports"
        mask = "*.kt"
    }
}