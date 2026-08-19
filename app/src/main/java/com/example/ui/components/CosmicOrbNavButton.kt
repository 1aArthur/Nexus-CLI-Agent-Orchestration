package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicNeonCyan
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.CosmicWhite

@Composable
fun CosmicOrbNavButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb_rotation"
    )

    Box(
        modifier = modifier
            .size(64.dp)
            .offset(y = (-14).dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Outer Cosmic Nebula Flare Glow
        Canvas(
            modifier = Modifier
                .size(64.dp)
                .scale(pulseScale)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CosmicWhite.copy(alpha = glowAlpha * 0.45f),
                        CosmicCyan.copy(alpha = glowAlpha * 0.35f),
                        CosmicPurple.copy(alpha = glowAlpha * 0.2f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width / 2f
                ),
                center = center,
                radius = size.width / 2f
            )
        }

        // 2. Cosmic Orb Center Core
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(elevation = 12.dp, shape = CircleShape, ambientColor = CosmicCyan, spotColor = CosmicWhite)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CosmicWhite,
                            Color(0xFFE0F2FE),
                            Color(0xFF0F172A),
                            CosmicBlack
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            CosmicWhite,
                            CosmicCyan,
                            CosmicPurple,
                            CosmicWhite
                        )
                    ),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 32.dp, color = CosmicCyan),
                    onClick = onClick
                )
                .testTag("cosmic_orb_button"),
            contentAlignment = Alignment.Center
        ) {
            // Rotating star vortex aura
            Image(
                painter = painterResource(id = R.drawable.img_cosmic_orb_portal),
                contentDescription = "Cosmic Nexus Portal",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .rotate(rotation),
                contentScale = ContentScale.Crop
            )

            // Inner Ring Sparkle
            Canvas(modifier = Modifier.size(44.dp)) {
                drawCircle(
                    color = CosmicWhite.copy(alpha = glowAlpha * 0.6f),
                    radius = 2.dp.toPx(),
                    center = Offset(size.width * 0.5f, size.height * 0.5f)
                )
            }
        }
    }
}
