package org.jetbrains.plugins.template.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.plugins.template.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = koinViewModel()) {
    val uiState by settingsViewModel.uiState.collectAsState()

    SettingsContent(state =  uiState, onDarkModeChanged = { settingsViewModel.onDarkModeChanged(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(state: SettingsViewModel.UiState, onDarkModeChanged: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Settings") })

        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Dark mode", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.darkMode,
                    onCheckedChange = onDarkModeChanged
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    SettingsContent(
        state = SettingsViewModel.UiState(),
        onDarkModeChanged = {}
    )
}