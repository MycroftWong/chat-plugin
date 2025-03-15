package org.jetbrains.plugins.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.plugins.template.model.Bot
import org.jetbrains.plugins.template.model.Me
import org.jetbrains.plugins.template.model.Message
import org.jetbrains.plugins.template.model.ollama.ChatMessage
import org.jetbrains.plugins.template.model.ollama.ChatRequest
import org.jetbrains.plugins.template.model.ollama.ChatResponse
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ChatViewModel : ViewModel() {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        engine {
            requestTimeout = 600_000
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    private val _loading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _messages = MutableStateFlow<List<Message>>(emptyList())

    val uiState: StateFlow<UiState> = combine(_loading, _error, _messages) { loading, error, messages ->
        UiState(loading, error, messages)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun chat(message: String) {
        viewModelScope.launch {
            if (_loading.value) return@launch
            _loading.value = true

            _messages.update { old ->
                old + Me(message) + Bot("")
            }

            runCatching {
                httpClient.preparePost("http://127.0.0.1:11434/api/chat") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        ChatRequest(
                            messages = listOf(
                                ChatMessage("system", "You're a kotlin professor"),
                                ChatMessage("user", message)
                            )
                        )
                    )
                }.execute { response ->
                    if (!response.status.isSuccess()) {
                        _error.value = "Failed to send message"
                    } else {
                        val responseMessage = StringBuilder()
                        val channel: ByteReadChannel = response.body()
                        while (!channel.exhausted()) {
                            val line = channel.readUTF8Line() ?: break
                            val chatResponse: ChatResponse = json.decodeFromString(line)
                            responseMessage.append(chatResponse.message.content)
                            _messages.update { old ->
                                val last = old.last() as Bot
                                old.dropLast(1) + last.copy(message = responseMessage.toString())
                            }
                        }
                    }
                }
            }.onFailure {
                _error.value = it.message
            }

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