package org.kmp.playground.shorthub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import org.kmp.playground.shorthub.hub.presentation.ui.AddShortcutScene
import org.kmp.playground.shorthub.hub.presentation.ui.SearchShortcutScene
import org.kmp.playground.shorthub.pref.presentation.ui.PrefsScene
import org.kmp.playground.shorthub.shared.ui.RecordingOverlay

@Composable
fun WindowScope.PrefsApp(onClose: () -> Unit) {
    MaterialTheme {
        WindowFrame(onClose = onClose) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                RecordingOverlay {
                    PrefsScene()
                }
            }
        }
    }
}

@Composable
fun WindowScope.AddShortcutApp(onClose: () -> Unit) {
    MaterialTheme {
        WindowFrame(onClose = onClose) {
            AddShortcutScene(showBackgroundDim = false)
        }
    }
}

@Composable
fun WindowScope.SearchShortcutApp(onClose: () -> Unit) {
    MaterialTheme {
        WindowFrame(onClose = onClose) {
            SearchShortcutScene(showBackgroundDim = false)
        }
    }
}
