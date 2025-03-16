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
import wang.mycroft.core.data.model.deepseek.ChatMessage
import wang.mycroft.core.data.model.deepseek.ChatRequest
import wang.mycroft.core.data.model.deepseek.ChatResponse

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

        val statement = httpClient.preparePost("https://api.deepseek.com/chat/completions") {
            header("Authorization", "Bearer <Deepseek API Key>")
            contentType(ContentType.Application.Json)
            setBody(
                ChatRequest(
//                    model = "deepseek-chat",
                    model = "deepseek-reasoner",
                    messages = listOf(
                        ChatMessage(role = "system", content = "You are a helpful assistant."),
                        ChatMessage(role = "user", content = input)
                    ),
                    stream = true
                )
            )
        }
        return statement.execute { response ->
            if (!response.status.isSuccess()) {
                throw Exception("Failed to send message")
            }

            val responseThink = StringBuilder()
            val responseMessage = StringBuilder()
            val channel: ByteReadChannel = response.bodyAsChannel()
            while (!channel.exhausted()) {
                val line = channel.readUTF8Line() ?: break

                val content = line.removePrefix("data: ")
                runCatching {
                    if (content != "[DONE]" && content.trim().isNotEmpty()) {
                        val chatResponse: ChatResponse = json.decodeFromString(content)
                        chatResponse.choices
                            .sortedBy { it.index }
                            .forEach {
                                if (it.delta.reasoningContent != null) {
                                    responseThink.append(it.delta.reasoningContent)
                                } else {
                                    responseMessage.append(it.delta.content)
                                }
                            }
                    }
                }.onFailure {
                    println("error: $line, $it")
                }

                _messages.update { old ->
                    val last = old.last() as Bot
                    old.dropLast(1) + last.copy(
                        message = responseMessage.toString(),
                        think = responseThink.toString()
                    )
                }
            }
            ChatResult(responseMessage.toString())
        }
    }

}
