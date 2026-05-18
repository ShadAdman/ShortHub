package org.kmp.playground.shorthub

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kmp.playground.shorthub.db.di.dbModule
import org.kmp.playground.shorthub.hub.di.hubModule
import org.kmp.playground.shorthub.pref.di.prefModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(dbModule, hubModule, prefModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ShortHub",
        ) {
            App()
        }
    }
}
