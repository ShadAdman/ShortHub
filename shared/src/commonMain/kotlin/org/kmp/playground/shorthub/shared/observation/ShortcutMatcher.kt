package org.kmp.playground.shorthub.shared.observation

object ShortcutMatcher {
    fun matches(event: KeyEvent, shortcutStr: String): Boolean {
        if (!event.isPressed) return false
        
        val parts = shortcutStr.split("+").map { normalize(it.trim()) }
        val key = parts.last()
        val modifiers = parts.dropLast(1)
        
        val eventKey = normalize(event.key)
        if (eventKey != key) return false
        
        val ctrlNeeded = modifiers.contains("ctrl")
        val altNeeded = modifiers.contains("alt")
        val shiftNeeded = modifiers.contains("shift")
        val metaNeeded = modifiers.contains("meta")
        
        return event.ctrlPressed == ctrlNeeded &&
               event.altPressed == altNeeded &&
               event.shiftPressed == shiftNeeded &&
               event.metaPressed == metaNeeded
    }

    private fun normalize(key: String): String {
        return when (val lower = key.lowercase()) {
            "escape", "esc" -> "esc"
            "control", "ctrl" -> "ctrl"
            "alt", "option" -> "alt"
            "shift" -> "shift"
            "meta", "win", "cmd", "command", "windows" -> "meta"
            "enter", "return" -> "enter"
            "space", "spacebar" -> "space"
            else -> lower
        }
    }
}
