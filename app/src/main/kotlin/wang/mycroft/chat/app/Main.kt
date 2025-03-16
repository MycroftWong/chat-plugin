package wang.mycroft.chat.app

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import wang.mycroft.chat.app.wang.mycroft.chat.app.ChatToolWindow

fun main() {
    val state = WindowState(size = DpSize(360.dp, 720.dp))
    singleWindowApplication(
        state = state,
        title = "Chat",
    ) {
        ChatToolWindow()
    }
}
