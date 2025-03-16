plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)

    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.compose)
}

group = "wang.mycroft.feature.chat"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)

    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

//    implementation(libs.compose.markdown)
//    implementation(libs.richeditor.compose)
    implementation(libs.multiplatform.markdown.renderer.m3)
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}