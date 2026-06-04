package org.kmp.playground.shorthub

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    // Global Shortcut Handler
    combine(
        repository.getPrefs(),
        inputObserver.keyEvents
    ) { prefs, event ->
        if (ShortcutMatcher.matches(event, prefs.addNewShortcut)) {
            navigationService.showAdd()
        } else if (ShortcutMatcher.matches(event, prefs.searchShortcut)) {
            navigationService.showSearch()
        }
    }.launchIn(scope)

    inputObserver.start()

    application {
        val isVisible by navigationService.isWindowVisible.collectAsState()
        val trayState = rememberTrayState()
        
        Tray(
            state = trayState,
            icon = ColorPainter(Color.Cyan), // Simple placeholder icon
            menu = {
                Item("Show ShortHub", onClick = { navigationService.setWindowVisible(true) })
                Separator()
                Item("Exit", onClick = {
                    inputObserver.stop()
                    exitApplication()
                })
            }
        )

        Window(
            onCloseRequest = {
                navigationService.setWindowVisible(false)
            },
            visible = isVisible,
            title = "ShortHub",
            undecorated = true,
            transparent = true,
        ) {
            App(
                onClose = {
                    navigationService.setWindowVisible(false)
                }
            )
        }
    }
}
