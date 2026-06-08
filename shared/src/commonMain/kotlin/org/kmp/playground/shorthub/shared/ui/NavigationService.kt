package org.kmp.playground.shorthub.shared.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationService {
    private val _isAddVisible = MutableStateFlow(false)
    val isAddVisible = _isAddVisible.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible = _isSearchVisible.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private val _isWindowVisible = MutableStateFlow(true)
    val isWindowVisible = _isWindowVisible.asStateFlow()

    private val _isPrefsVisible = MutableStateFlow(true)
    val isPrefsVisible = _isPrefsVisible.asStateFlow()

    fun showAdd() {
        _isWindowVisible.value = true
        _isAddVisible.value = true
        _isSearchVisible.value = false
        _isPrefsVisible.value = false
    }

    fun showSearch() {
        _isWindowVisible.value = true
        _isSearchVisible.value = true
        _isAddVisible.value = false
        _isPrefsVisible.value = false
    }

    fun showPrefs() {
        _isWindowVisible.value = true
        _isPrefsVisible.value = true
        _isAddVisible.value = false
        _isSearchVisible.value = false
    }

    fun toggleWindow() {
        _isWindowVisible.value = !_isWindowVisible.value
        if (_isWindowVisible.value) {
            _isPrefsVisible.value = true
        }
    }

    fun setWindowVisible(visible: Boolean) {
        _isWindowVisible.value = visible
        if (visible) {
            _isPrefsVisible.value = true
            _isAddVisible.value = false
            _isSearchVisible.value = false
        }
    }

    fun hideAll() {
        _isAddVisible.value = false
        _isSearchVisible.value = false
        _isRecording.value = false
        _isWindowVisible.value = false
    }

    fun setRecording(recording: Boolean) {
        _isRecording.value = recording
    }
}
