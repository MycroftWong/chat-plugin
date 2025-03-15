package org.jetbrains.plugins.template.model.ollama

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val messages: List<ChatMessage>,
    val model: String = "qwen2.5-coder:14b"
)
