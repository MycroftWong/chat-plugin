package wang.mycroft.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import wang.mycroft.core.data.settings.SettingsRepository

@KoinViewModel
class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = settingsRepository.userPreferences.map {
        UiState(darkMode = it.darkMode)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun onDarkModeChanged(flag: Boolean) {
        viewModelScope.launch {
            if (_loading.value) return@launch
            _loading.value = true

            settingsRepository.setDarkMode(flag)

            _loading.value = false
        }
    }


    data class UiState(
        val darkMode: Boolean = false,
    )
}