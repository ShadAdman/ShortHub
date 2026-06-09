package org.kmp.playground.shorthub

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.kmp.playground.shorthub.hub.presentation.ui.AddShortcutScene
import org.kmp.playground.shorthub.hub.presentation.ui.SearchShortcutScene
import org.kmp.playground.shorthub.pref.presentation.ui.PrefsScene
import org.kmp.playground.shorthub.shared.ui.RecordingOverlay

@Composable
@Preview
fun App() {
    MaterialTheme {
        RecordingOverlay {
            Box {
                PrefsScene()
                SearchShortcutScene()
                AddShortcutScene()
            }
        }
    }
}
