package wang.mycroft.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import wang.mycroft.core.data.chat.ChatRepository
import wang.mycroft.core.data.model.Message

@KoinViewModel
class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState> = combine(_loading, _error, chatRepository.messages) { loading, error, messages ->
        UiState(loading, error, messages)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun chat(message: String) {
        viewModelScope.launch {
            if (_loading.value) return@launch
            _loading.value = true

            val result = chatRepository.chat(message)

            _loading.value = false
        }
    }

    fun onErrorHandled() {
        _error.value = null
    }

    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val messages: List<Message> = emptyList(),
    )
}