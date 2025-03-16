package wang.mycroft.chat.app.wang.mycroft.chat.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Rtt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.module
import wang.mycroft.feature.chat.Chat
import wang.mycroft.feature.chat.chatScreen
import wang.mycroft.feature.settings.Settings
import wang.mycroft.feature.settings.settingsScreen

@Composable
fun ChatToolWindow() {
    KoinApplication(application = {
        modules(AppModule().module)
    }) {
        MaterialTheme {
            val snackbarHostState = remember { SnackbarHostState() }

            val navController = rememberNavController()

            val backStack by navController.currentBackStackEntryAsState()
            LaunchedEffect(backStack) {
                println("currentDestination: ${navController.currentDestination?.route}")
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
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
                                selected = navController.currentDestination?.hasRoute(screen::class) == true,
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                                onClick = {
                                    if (navController.currentDestination?.hasRoute(screen::class) != true) {
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
                }) { paddingValues ->

                NavHost(
                    navController = navController,
                    startDestination = Chat,
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                ) {
                    chatScreen(onShowSnackbar = { snackbarHostState.showSnackbar(it) })

                    settingsScreen()
                }
            }
        }

    }
}
