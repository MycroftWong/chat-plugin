package org.jetbrains.plugins.template.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Settings

fun NavGraphBuilder.settingsScreen() {
    composable<Settings> {
        SettingsScreen()
    }
}