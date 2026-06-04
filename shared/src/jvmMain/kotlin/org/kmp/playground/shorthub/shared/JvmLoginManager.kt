package org.kmp.playground.shorthub.shared

import java.io.File

class JvmLoginManager : LoginManager {
    override fun setLaunchAtLogin(enabled: Boolean) {
        val os = System.getProperty("os.name").lowercase()
        if (os.contains("win")) {
            setWindowsLaunchAtLogin(enabled)
        }
    }

    private fun setWindowsLaunchAtLogin(enabled: Boolean) {
        try {
            val jarPath = File(JvmLoginManager::class.java.protectionDomain.codeSource.location.toURI()).absolutePath
            val command = if (enabled) {
                "reg add \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \"ShortHub\" /t REG_SZ /d \"\\\"$jarPath\\\" --minimized\" /f"
            } else {
                "reg delete \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \"ShortHub\" /f"
            }
            Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

actual fun createLoginManager(): LoginManager = JvmLoginManager()
