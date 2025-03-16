plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)

    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.compose)
}

group = "wang.mycroft.core.ui"
version = "0.0.1"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.swing)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)

    implementation(project(":core:data"))

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
