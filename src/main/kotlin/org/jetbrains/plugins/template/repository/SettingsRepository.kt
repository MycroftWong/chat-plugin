package org.jetbrains.plugins.template.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

interface SettingsRepository {
    val userPreferences: Flow<UserPreferences>

    suspend fun setDarkMode(enabled: Boolean)
}

@Single(binds = [SettingsRepository::class])
internal class SettingsRepositoryImpl : SettingsRepository {
    private val _userPreferences = MutableStateFlow(UserPreferences())
    override val userPreferences: Flow<UserPreferences> = _userPreferences.asStateFlow()

    override suspend fun setDarkMode(enabled: Boolean) {
        _userPreferences.update {
            it.copy(darkMode = enabled)
        }
    }
}