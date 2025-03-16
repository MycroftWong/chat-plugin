package wang.mycroft.core.data.model.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val model: String,
    @SerialName("created_at") val createdAt: String,
    val message: ChatMessage
)