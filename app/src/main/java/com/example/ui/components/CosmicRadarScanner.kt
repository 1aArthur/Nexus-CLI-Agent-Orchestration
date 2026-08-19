package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CosmicBorderBright
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicNeonCyan
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CosmicRadarScanner(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    radarColor: Color = CosmicCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_transition")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_rotation"
    )

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_pulse"
    )

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        val maxRadius = this.size.width / 2f

        // Concentric Rings
        drawCircle(
            color = CosmicBorderBright.copy(alpha = 0.4f),
            radius = maxRadius,
            center = center,
            style = Stroke(width = 1f)
        )
        drawCircle(
            color = CosmicBorderBright.copy(alpha = 0.35f),
            radius = maxRadius * 0.66f,
            center = center,
            style = Stroke(width = 1f)
        )
        drawCircle(
            color = CosmicBorderBright.copy(alpha = 0.3f),
            radius = maxRadius * 0.33f,
            center = center,
            style = Stroke(width = 1f)
        )

        // Crosshairs
        drawLine(
            color = CosmicBorderBright.copy(alpha = 0.25f),
            start = Offset(center.x, 0f),
            end = Offset(center.x, this.size.height),
            strokeWidth = 1f
        )
        drawLine(
            color = CosmicBorderBright.copy(alpha = 0.25f),
            start = Offset(0f, center.y),
            end = Offset(this.size.width, center.y),
            strokeWidth = 1f
        )

        // Expanding Pulse
        drawCircle(
            color = radarColor.copy(alpha = (1f - pulseRadius) * 0.5f),
            radius = maxRadius * pulseRadius,
            center = center,
            style = Stroke(width = 1.5f)
        )

        // Rotating Sweep Beam
        val rad = Math.toRadians(rotationAngle.toDouble())
        val beamEnd = Offset(
            x = center.x + (maxRadius * cos(rad)).toFloat(),
            y = center.y + (maxRadius * sin(rad)).toFloat()
        )

        drawLine(
            brush = Brush.radialGradient(
                colors = listOf(radarColor, Color.Transparent),
                center = center,
                radius = maxRadius
            ),
            start = center,
            end = beamEnd,
            strokeWidth = 2f
        )

        // Center Blip
        drawCircle(
            color = CosmicNeonCyan,
            radius = 2.5f,
            center = center
        )
    }
}
