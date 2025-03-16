package wang.mycroft.core.data.chat

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import wang.mycroft.core.data.model.Bot
import wang.mycroft.core.data.model.Me
import wang.mycroft.core.data.model.Message
import wang.mycroft.core.data.model.ollama.ChatMessage
import wang.mycroft.core.data.model.ollama.ChatRequest
import wang.mycroft.core.data.model.ollama.ChatResponse

interface ChatRepository {
    val messages: Flow<List<Message>>
    suspend fun chat(input: String): ChatResult
}

@Single(binds = [ChatRepository::class])
internal class ChatRepositoryImpl(
    private val json: Json,
    private val httpClient: HttpClient
) : ChatRepository {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    override val messages: Flow<List<Message>> = _messages.asStateFlow()

    override suspend fun chat(input: String): ChatResult {
        _messages.update { old ->
            old + Me(input) + Bot("")
        }

        val statement = httpClient.preparePost("http://127.0.0.1:11434/api/chat") {
            contentType(ContentType.Application.Json)
            setBody(
                ChatRequest(
                    messages = listOf(
                        ChatMessage("system", "You're a AI assistant!"),
                        ChatMessage("user", input)
                    )
                )
            )
        }
        return statement.execute { response ->
            if (!response.status.isSuccess()) {
                throw Exception("Failed to send message")
            }

            val responseMessage = StringBuilder()
            val channel: ByteReadChannel = response.bodyAsChannel()
            while (!channel.exhausted()) {

                val line = channel.readUTF8Line() ?: break
                val chatResponse: ChatResponse = json.decodeFromString(line)
                responseMessage.append(chatResponse.message.content)

                _messages.update { old ->
                    val last = old.last() as Bot
                    old.dropLast(1) + last.copy(message = responseMessage.toString())
                }
            }
            ChatResult(responseMessage.toString())
        }
    }

}
