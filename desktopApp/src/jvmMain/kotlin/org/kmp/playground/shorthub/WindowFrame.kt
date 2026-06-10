package org.kmp.playground.shorthub

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.WindowFrame(
    onClose: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.sweepGradient(
        colors = listOf(
            Color.Cyan,
            Color.Magenta,
            Color.Yellow,
            Color.Cyan
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
//                rotate(angle) {
//                    drawRect(
//                        brush = brush,
//                        style = Stroke(width = 16.dp.toPx())
//                    )
//                }
                drawContent()
            }
            .padding(6.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            WindowDraggableArea {
                Box(Modifier.fillMaxWidth().height(48.dp))
            }

            content()

            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Text(
                    text = "✕",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
