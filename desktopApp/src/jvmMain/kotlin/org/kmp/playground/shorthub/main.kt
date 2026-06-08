package org.kmp.playground.shorthub

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.kmp.playground.shorthub.db.di.dbModule
import org.kmp.playground.shorthub.hub.di.hubModule
import org.kmp.playground.shorthub.pref.di.prefModule
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.kmp.playground.shorthub.shared.di.sharedModule
import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.observation.ShortcutMatcher
import org.kmp.playground.shorthub.shared.ui.NavigationService
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs

fun main(args: Array<String>) {
    startKoin {
        modules(dbModule, hubModule, prefModule, sharedModule)
    }
    
    val inputObserver = getKoin().get<InputObserver>()
    val navigationService = getKoin().get<NavigationService>()
    val repository = getKoin().get<ShortcutRepository>()
    val scope = MainScope()

    if (args.contains("--minimized")) {
        navigationService.setWindowVisible(false)
    }

    val prefsState = repository.getPrefs().stateIn(scope, SharingStarted.Eagerly, ShortcutPrefs())

    // Global Shortcut Handler
    inputObserver.keyEvents.onEach { event ->
        val prefs = prefsState.value
        if (ShortcutMatcher.matches(event, prefs.addNewShortcut)) {
            navigationService.showAdd()
        } else if (ShortcutMatcher.matches(event, prefs.searchShortcut)) {
            navigationService.showSearch()
        } else if (ShortcutMatcher.matches(event, prefs.closeWindowShortcut)) {
            navigationService.hideAll()
        }
    }.launchIn(scope)

    inputObserver.start()

    application {
        val isVisible by navigationService.isWindowVisible.collectAsState()
        val isPrefsVisible by navigationService.isPrefsVisible.collectAsState()
        val isAddVisible by navigationService.isAddVisible.collectAsState()
        val isSearchVisible by navigationService.isSearchVisible.collectAsState()
        
        val trayState = rememberTrayState()
        
        Tray(
            state = trayState,
            icon = ColorPainter(Color.Cyan),
            menu = {
                Item("Show ShortHub", onClick = { navigationService.setWindowVisible(true) })
                Separator()
                Item("Exit", onClick = {
                    inputObserver.stop()
                    exitApplication()
                })
            }
        )

        if (isVisible && isPrefsVisible) {
            Window(
                onCloseRequest = {
                    navigationService.setWindowVisible(false)
                    navigationService.hideAll()
                },
                title = "ShortHub Settings",
                undecorated = true,
                transparent = true,
                onPreviewKeyEvent = {
                    if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                        navigationService.setWindowVisible(false)
                        navigationService.hideAll()
                        true
                    } else false
                },
                state = rememberWindowState(width = 800.dp, height = 700.dp, position = WindowPosition(Alignment.Center))
            ) {
                PrefsApp(
                    onClose = {
                        navigationService.setWindowVisible(false)
                        navigationService.hideAll()
                    }
                )
            }
        }

        if (isAddVisible) {
            Window(
                onCloseRequest = { navigationService.hideAll() },
                title = "Add Shortcut",
                undecorated = true,
                transparent = true,
                alwaysOnTop = true,
                onPreviewKeyEvent = {
                    if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                        navigationService.hideAll()
                        true
                    } else false
                },
                state = rememberWindowState(width = 400.dp, height = 480.dp, position = WindowPosition(Alignment.Center))
            ) {
                AddShortcutApp(onClose = { navigationService.hideAll() })
            }
        }

        if (isSearchVisible) {
            Window(
                onCloseRequest = { navigationService.hideAll() },
                title = "Search Shortcuts",
                undecorated = true,
                transparent = true,
                alwaysOnTop = true,
                onPreviewKeyEvent = {
                    if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                        navigationService.hideAll()
                        true
                    } else false
                },
                state = rememberWindowState(width = 500.dp, height = 400.dp, position = WindowPosition(Alignment.Center))
            ) {
                SearchShortcutApp(onClose = { navigationService.hideAll() })
            }
        }
    }
}
