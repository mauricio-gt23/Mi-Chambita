pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MiChambita"
include(":app")
include(":core:domain")
include(":core:common")
include(":core:data")
include(":core:ui")
include(":router")
include(":feature:auth")
include(":feature:home")
include(":feature:producto")
include(":feature:inventario")
