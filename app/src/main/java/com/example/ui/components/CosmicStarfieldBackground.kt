package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.CosmicBlack
import com.example.ui.theme.CosmicDark
import kotlin.random.Random

data class StarParticle(
    val xRatio: Float,
    val yRatio: Float,
    val baseRadius: Float,
    val baseAlpha: Float,
    val speed: Float,
    val twinkleSpeed: Float,
    val color: Color
)

@Composable
fun CosmicStarfieldBackground(
    modifier: Modifier = Modifier,
    particleCount: Int = 90,
    showNebula: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cosmic_stars")
    
    // Smooth infinite time progression
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "star_movement"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nebula_pulse"
    )

    // Pre-generate deterministic particles
    val particles = remember(particleCount) {
        val rand = Random(42)
        List(particleCount) {
            val isBright = rand.nextFloat() > 0.85f
            val isCyanTint = rand.nextFloat() > 0.88f
            val isPurpleTint = rand.nextFloat() > 0.92f
            
            val starColor = when {
                isCyanTint -> Color(0xFFE0F7FF)
                isPurpleTint -> Color(0xFFF3E8FF)
                isBright -> Color(0xFFFFFFFF)
                else -> Color(0xFFF0F4F8)
            }

            StarParticle(
                xRatio = rand.nextFloat(),
                yRatio = rand.nextFloat(),
                baseRadius = if (isBright) rand.nextFloat() * 1.8f + 1.2f else rand.nextFloat() * 1.2f + 0.6f,
                baseAlpha = if (isBright) rand.nextFloat() * 0.4f + 0.6f else rand.nextFloat() * 0.35f + 0.25f,
                speed = rand.nextFloat() * 0.015f + 0.005f,
                twinkleSpeed = rand.nextFloat() * 3.0f + 1.5f,
                color = starColor
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBlack)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Deep Space Radial Nebula Gradient
            if (showNebula) {
                val nebulaCenter = Offset(width * 0.5f, height * 0.3f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1838BDF8 * pulse.toInt()).copy(alpha = 0.07f * pulse),
                            Color(0x15818CF8).copy(alpha = 0.05f * pulse),
                            Color(0x00000000)
                        ),
                        center = nebulaCenter,
                        radius = width * 0.85f
                    ),
                    center = nebulaCenter,
                    radius = width * 0.85f
                )

                val bottomNebulaCenter = Offset(width * 0.5f, height * 0.9f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x12A855F7).copy(alpha = 0.06f * pulse),
                            Color(0x00000000)
                        ),
                        center = bottomNebulaCenter,
                        radius = width * 0.65f
                    ),
                    center = bottomNebulaCenter,
                    radius = width * 0.65f
                )
            }

            // 2. Render Moving & Twinkling White Stars
            particles.forEach { particle ->
                // Drift vertically with loop
                val currentY = ((particle.yRatio * height + time * particle.speed * 40f) % height)
                val currentX = particle.xRatio * width
                
                // Twinkle oscillation
                val twinkle = (kotlin.math.sin(time * particle.twinkleSpeed + particle.xRatio * 10f) + 1f) / 2f
                val alpha = (particle.baseAlpha * (0.4f + 0.6f * twinkle)).coerceIn(0.1f, 1.0f)
                val radius = particle.baseRadius * (0.85f + 0.3f * twinkle)

                // Draw star glow if bright
                if (particle.baseRadius > 1.8f) {
                    drawCircle(
                        color = particle.color.copy(alpha = alpha * 0.25f),
                        radius = radius * 2.5f,
                        center = Offset(currentX, currentY)
                    )
                }

                // Draw star core
                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = radius,
                    center = Offset(currentX, currentY)
                )
            }
        }

        // Screen Foreground Content
        content()
    }
}
