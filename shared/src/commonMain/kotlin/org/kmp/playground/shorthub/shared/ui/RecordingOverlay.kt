package org.kmp.playground.shorthub.shared.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun RecordingOverlay(
    navigationService: NavigationService = koinInject(),
    content: @Composable () -> Unit
) {
    val isRecording by navigationService.isRecording.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        
        AnimatedVisibility(
            visible = isRecording,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 2000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .drawWithCache {
                        val brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Cyan,
                                Color.Magenta,
                                Color.Yellow,
                                Color.Cyan
                            ),
                            start = Offset(offset - 1000f, offset - 1000f),
                            end = Offset(offset, offset)
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = brush,
                                style = Stroke(width = 6.dp.toPx())
                            )
                        }
                    }
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = "Recording Shortcut... Press any key combination",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
