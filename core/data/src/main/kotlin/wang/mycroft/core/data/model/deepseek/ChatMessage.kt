package wang.mycroft.core.data.model.deepseek

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String,
)
