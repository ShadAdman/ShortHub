package org.kmp.playground.shorthub.shared

interface LoginManager {
    fun setLaunchAtLogin(enabled: Boolean)
}

expect fun createLoginManager(): LoginManager
