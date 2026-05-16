package org.kmp.playground.shorthub.hub.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val repository: ShortcutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        _query
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.searchShortcuts(query)
                }
            }
            .onEach { results ->
                _state.update { it.copy(results = results.take(10), isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Show -> _state.update { it.copy(isVisible = intent.show) }
            is SearchIntent.Dismiss -> _state.update { it.copy(isVisible = false) }
            is SearchIntent.UpdateQuery -> {
                _state.update { it.copy(query = intent.query, isLoading = true) }
                _query.value = intent.query
            }
        }
    }
}
