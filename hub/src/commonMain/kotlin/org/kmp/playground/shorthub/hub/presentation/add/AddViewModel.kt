package org.kmp.playground.shorthub.hub.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kmp.playground.shorthub.db.domain.model.Shortcut
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository

class AddViewModel(
    private val repository: ShortcutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    fun onIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.Show -> _state.update { it.copy(isVisible = intent.show) }
            is AddIntent.Dismiss -> _state.update { it.copy(isVisible = false) }
            is AddIntent.SaveShortcut -> saveShortcut(intent.title, intent.shortcut)
        }
    }

    private fun saveShortcut(title: String, shortcut: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                repository.addShortcut(Shortcut(title = title, shortcut = shortcut))
                _state.update { it.copy(isVisible = false, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
