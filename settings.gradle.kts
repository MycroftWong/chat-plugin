rootProject.name = "Chat Plugin"

pluginManagement {
    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        kotlin("plugin.compose").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
        id("com.google.devtools.ksp").version("2.1.10-1.0.31")
    }
}
