package wang.mycroft.core.data.model.deepseek

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<ChatMessage>,
    val stream: Boolean = false,
    val temperature: Float = 0F,
)