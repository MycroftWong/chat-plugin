package wang.mycroft.core.data.model.deepseek


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    @SerialName("object")
    val obj: String,
    val created: Long,
    val model: String,
    @SerialName("system_fingerprint")
    val system_fingerprint: String,
    val choices: List<Choice>,
)

@Serializable
data class Choice(
    val index: Int,
    val delta: Delta,
    val finish_reason: String? = null,
//    val logprobs: String? = null,
)

@Serializable
data class Delta(
    val role: String? = null,
    val content: String? = null,
    @SerialName("reasoning_content")
    val reasoningContent: String? = null,
)