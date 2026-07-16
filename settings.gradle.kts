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
include(":common")
include(":domain")
include(":data")
include(":ui")
include(":router")
include(":feature:auth")
include(":feature:home")
include(":feature:item")
include(":feature:inventario")
include(":feature:profile")
include(":feature:history")
