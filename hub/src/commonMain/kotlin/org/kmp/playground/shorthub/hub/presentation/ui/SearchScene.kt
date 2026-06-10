package org.kmp.playground.shorthub.hub.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.kmp.playground.shorthub.db.domain.model.Shortcut
import org.kmp.playground.shorthub.hub.presentation.search.SearchIntent
import org.kmp.playground.shorthub.hub.presentation.search.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchShortcutScene(
    viewModel: SearchViewModel = koinViewModel(),
    showBackgroundDim: Boolean = true
) {
    val state by viewModel.state.collectAsState()
    
    SearchShortcutPopup(
        isVisible = state.isVisible,
        onDismiss = { viewModel.onIntent(SearchIntent.Dismiss) },
        searchQuery = state.query,
        onQueryChange = { viewModel.onIntent(SearchIntent.UpdateQuery(it)) },
        results = state.results,
        showBackgroundDim = showBackgroundDim
    )
}

@Composable
fun SearchShortcutPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    results: List<Shortcut>,
    showBackgroundDim: Boolean = true
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(
            initialScale = 0.9f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        exit = fadeOut() + scaleOut(targetScale = 0.9f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (showBackgroundDim) Color.Black.copy(alpha = 0.4f) else Color.Transparent)
                .then(if (showBackgroundDim) Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 16.dp) else Modifier),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = if (showBackgroundDim) {
                    Modifier.widthIn(max = 500.dp)
                } else {
                    Modifier.fillMaxSize()
                },
                shape = if (showBackgroundDim) RoundedCornerShape(28.dp) else RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = if (showBackgroundDim) 12.dp else 0.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search shortcuts...") },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    if (results.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(results) { shortcut ->
                                ShortcutResultItem(shortcut)
                            }
                        }
                    } else if (searchQuery.isNotBlank()) {
                        Text(
                            "No results found",
                            modifier = Modifier.padding(32.dp).align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortcutResultItem(shortcut: Shortcut) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = shortcut.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = shortcut.shortcut,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
