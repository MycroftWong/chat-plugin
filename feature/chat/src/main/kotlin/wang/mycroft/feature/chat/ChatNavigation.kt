package wang.mycroft.feature.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Chat

fun NavGraphBuilder.chatScreen(
    onShowSnackbar: suspend (String) -> Unit
) {
    composable<Chat> {
        ChatScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
