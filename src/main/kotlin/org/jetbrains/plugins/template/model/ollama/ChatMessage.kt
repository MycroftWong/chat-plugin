package org.jetbrains.plugins.template.model.ollama

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)
