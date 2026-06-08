package org.kmp.playground.shorthub.db.domain.model

data class ShortcutPrefs(
    val addNewShortcut: String = "Ctrl+Alt+A",
    val searchShortcut: String = "Ctrl+Space",
    val closeWindowShortcut: String = "Esc",
    val launchAtLogin: Boolean = false
)
