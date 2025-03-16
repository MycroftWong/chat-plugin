package wang.mycroft.core.data.settings

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val darkMode: Boolean = false,
)