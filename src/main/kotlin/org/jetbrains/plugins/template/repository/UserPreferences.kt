package org.jetbrains.plugins.template.repository

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val darkMode: Boolean = false,
)