package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00363A),
    onPrimaryContainer = NeonCyan,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B0764),
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = NeonEmerald,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF064E3B),
    onTertiaryContainer = Color(0xFFA7F3D0),
    background = TerminalBackground,
    onBackground = TerminalText,
    surface = TerminalSurface,
    onSurface = TerminalText,
    surfaceVariant = TerminalSurfaceVariant,
    onSurfaceVariant = TerminalTextDim,
    outline = TerminalBorder,
    error = NeonRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Always use modern Cyberpunk dark theme for CLI / DevSecOps developer aesthetic
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

