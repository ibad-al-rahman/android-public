enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PublicSector"

include(":app")

include(":app:common:fp")
include(":app:common:mvi")
include(":app:common:base")
include(":app:common:network")
include(":app:common:resources")

include(":app:data:prayer-times-repository")

include(":app:screens:prayer-times")
include(":app:screens:settings")
