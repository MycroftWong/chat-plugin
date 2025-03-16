package wang.mycroft.core.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

sealed interface Message

data class Me(
    val message: String,
    val created: Instant = Clock.System.now(),
) : Message

data class Bot(
    val message: String,
    val think: String = "",
    val created: Instant = Clock.System.now(),
    val updated: Instant = Clock.System.now(),
) : Message
