package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicBorderBright
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicMuted
import com.example.ui.theme.CosmicNeonCyan
import com.example.ui.theme.CosmicWhite

@Composable
fun CosmicResourceGauge(
    title: String,
    percent: Int,
    modifier: Modifier = Modifier,
    size: Dp = 76.dp,
    activeColor: Color = CosmicCyan,
    subLabel: String = ""
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "gauge_val"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val strokeWidth = 5.dp.toPx()

                // Background track
                drawArc(
                    color = CosmicBorderBright.copy(alpha = 0.35f),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Foreground active arc
                val sweep = (animatedPercent / 100f) * 270f
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            activeColor.copy(alpha = 0.7f),
                            activeColor,
                            CosmicWhite
                        )
                    ),
                    startAngle = 135f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${percent}%",
                    color = CosmicWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = CosmicMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace
        )

        if (subLabel.isNotBlank()) {
            Text(
                text = subLabel,
                color = CosmicMuted.copy(alpha = 0.7f),
                fontSize = 9.sp
            )
        }
    }
}
