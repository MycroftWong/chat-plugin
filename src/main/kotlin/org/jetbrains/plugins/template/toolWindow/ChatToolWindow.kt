package org.jetbrains.plugins.template.toolWindow

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Rtt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.plugins.template.AppModule
import org.jetbrains.plugins.template.ui.Chat
import org.jetbrains.plugins.template.ui.Settings
import org.jetbrains.plugins.template.ui.chatScreen
import org.jetbrains.plugins.template.ui.settingsScreen
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.*

@Composable
fun ChatToolWindow() {
    KoinApplication(application = {
        modules(AppModule().module)
    }) {
        MaterialTheme {
            val snackbarHostState = remember { SnackbarHostState() }

            val navController = rememberNavController()

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    BottomNavigation {
                        listOf(Chat, Settings).forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        when (screen) {
                                            Chat -> Icons.AutoMirrored.Filled.Chat
                                            Settings -> Icons.AutoMirrored.Filled.Rtt
                                            else -> error("Unexpected screen: $screen")
                                        },
                                        contentDescription = screen.toString()
                                    )
                                },
                                label = { Text(screen.toString()) },
                                selected = navController.currentDestination?.route == screen.toString(),
                                onClick = {
                                    if (navController.currentDestination?.route == screen.toString()) {
                                        navController.navigate(screen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }) {
                NavHost(navController = navController, startDestination = Chat) {
                    chatScreen(onShowSnackbar = { snackbarHostState.showSnackbar(it) })

                    settingsScreen()
                }
            }
        }

    }
}
