package org.jetbrains.plugins.template.ui

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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import org.jetbrains.plugins.template.model.Bot
import org.jetbrains.plugins.template.model.Me
import org.jetbrains.plugins.template.viewmodel.ChatViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(chatViewModel: ChatViewModel = koinViewModel(), onShowSnackbar: suspend (String) -> Unit) {
    val state by chatViewModel.uiState.collectAsState()

    LaunchedEffect(state.error) {
        if (state.error != null) {
            onShowSnackbar("Error: ${state.error}")
            chatViewModel.onErrorHandled()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1F)) {
            items(state.messages) { message ->
                when (message) {
                    is Me -> MeMessageItem(message)
                    is Bot -> BotMessageItem(message)
                }
            }
        }
        var input by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.weight(1F),
            )
            Spacer(modifier = Modifier.width(8.dp))

            val isInputEmpty by remember(input) {
                derivedStateOf { input.isEmpty() }
            }
            Button(
                onClick = {
                    chatViewModel.chat(input)
                    input = ""
                },
                enabled = !isInputEmpty && !state.loading
            ) {
                Text("Send")
            }
        }
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}