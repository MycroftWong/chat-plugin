package wang.mycroft.feature.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import org.koin.compose.viewmodel.koinViewModel
import wang.mycroft.core.data.model.Bot
import wang.mycroft.core.data.model.Me

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatScreen(chatViewModel: ChatViewModel = koinViewModel(), onShowSnackbar: suspend (String) -> Unit) {
    val state by chatViewModel.uiState.collectAsState()

    LaunchedEffect(state.error) {
        if (state.error != null) {
            onShowSnackbar("Error: ${state.error}")
            chatViewModel.onErrorHandled()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Chat") },
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(modifier = Modifier.weight(1F).fillMaxWidth()) {
            items(state.messages) { message ->
                when (message) {
                    is Me -> MeMessageItem(message)
                    is Bot -> BotMessageItem(message)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        var input by remember { mutableStateOf("") }
        fun chat() {
            chatViewModel.chat(input.trim())
            input = ""
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = {
                    if (it.length < 200) {
                        input = it
                    }
                },
                label = { Text("Type a message") },
                modifier = Modifier.weight(1F).onKeyEvent {
                    if (it.key == Key.Enter) {
                        chat()
                        true
                    } else {
                        false
                    }
                },
            )
            Spacer(modifier = Modifier.width(8.dp))

            val isInputEmpty by remember(input) {
                derivedStateOf { input.isEmpty() }
            }
            Button(
                onClick = { chat() },
                enabled = !isInputEmpty && !state.loading
            ) {
                Text("Send")
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}


@Composable
private fun MeMessageItem(message: Me) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = message.created.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.weight(1F))
            Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun BotMessageItem(message: Bot) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Message, contentDescription = null)
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = message.created.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        if (message.think.trim().isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp)) {
                    Markdown(
                        message.think,
                        colors = markdownColor(
                            text = MaterialTheme.colorScheme.onSurface,
                            codeText = MaterialTheme.colorScheme.onSurface,
                            inlineCodeText = MaterialTheme.colorScheme.onSurface,
                            linkText = MaterialTheme.colorScheme.onSurface,
                            codeBackground = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            inlineCodeBackground = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            dividerColor = MaterialTheme.colorScheme.outlineVariant,
                            tableText = MaterialTheme.colorScheme.onSurface,
                            tableBackground = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                        ),
                        typography = markdownTypography(
                            text = MaterialTheme.typography.bodySmall,
                            code = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            inlineCode = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            quote = MaterialTheme.typography.bodySmall.plus(SpanStyle(fontStyle = FontStyle.Italic)),
                            paragraph = MaterialTheme.typography.bodyMedium,
                            ordered = MaterialTheme.typography.bodyMedium,
                            bullet = MaterialTheme.typography.bodyMedium,
                            list = MaterialTheme.typography.bodyMedium,
                            link = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline
                            ),
                            textLink = TextLinkStyles(
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline
                                ).toSpanStyle()
                            ),
                        ),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Markdown(
            message.message,
            colors = markdownColor(),
            typography = markdownTypography()
        )

        /*
                MarkdownText(
                    modifier = Modifier.padding(8.dp),
                    markdown = markdown,
                    maxLines = 3,
                    fontResource = R.font.montserrat_medium,
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 12.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Justify,
                    ),
        
                    )
        */
    }
}