package org.kmp.playground.shorthub.hub.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kmp.playground.shorthub.db.domain.model.Shortcut
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.ui.NavigationService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddViewModel(
    private val repository: ShortcutRepository,
    private val inputObserver: InputObserver,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    init {
        navigationService.isAddVisible
            .onEach { isVisible ->
                _state.update { it.copy(isVisible = isVisible) }
                if (isVisible) {
                    inputObserver.start()
                }
            }
            .launchIn(viewModelScope)

        inputObserver.keyEvents
            .onEach { event ->
                if (_state.value.isVisible && _state.value.isRecording && event.isPressed) {
                    handleRecordedKey(event)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleRecordedKey(event: org.kmp.playground.shorthub.shared.observation.KeyEvent) {
        val shortcut = buildString {
            val mods = mutableListOf<String>()
            if (event.ctrlPressed) mods.add("Ctrl")
            if (event.altPressed) mods.add("Alt")
            if (event.shiftPressed) mods.add("Shift")
            if (event.metaPressed) mods.add("Meta")
            
            append(mods.joinToString("+"))
            
            val key = event.key
            val isModifier = key in listOf("Ctrl", "Control", "Alt", "Alt Graph", "Shift", "Meta", "Command", "Windows")
            
            if (!isModifier && key.isNotEmpty() && event.isPressed) {
                if (mods.isNotEmpty()) append("+")
                append(key)
            }
        }
        _state.update { it.copy(recordedShortcut = shortcut) }
    }

    fun stopRecording() {
        _state.update { it.copy(isRecording = false) }
        navigationService.setRecording(false)
    }

    fun startRecording() {
        _state.update { it.copy(isRecording = true, recordedShortcut = "") }
        inputObserver.start()
        navigationService.setRecording(true)
    }

    fun onIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.Show -> navigationService.showAdd()
            is AddIntent.Dismiss -> navigationService.hideAll()
            is AddIntent.SaveShortcut -> saveShortcut(intent.title, intent.shortcut)
        }
    }

    private fun saveShortcut(title: String, shortcut: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                repository.addShortcut(Shortcut(title = title, shortcut = shortcut))
                _state.update { it.copy(isLoading = false) }
                navigationService.hideAll()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
