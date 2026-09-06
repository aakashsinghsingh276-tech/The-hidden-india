package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CleanMinimalismColorScheme = lightColorScheme(
    primary = MinimalNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = MinimalSkyContainer,
    onPrimaryContainer = MinimalNavySecondary,
    secondary = MinimalIndigoBrand,
    onSecondary = Color.White,
    secondaryContainer = MinimalIndigoBg,
    onSecondaryContainer = MinimalIndigoBrand,
    tertiary = MinimalEmeraldGem,
    onTertiary = Color.White,
    background = MinimalBgLight,
    onBackground = MinimalTextPrimary,
    surface = MinimalSurfaceLight,
    onSurface = MinimalTextPrimary,
    surfaceVariant = MinimalPillBg,
    onSurfaceVariant = MinimalTextSecondary,
    outline = MinimalSlateBorder,
    outlineVariant = MinimalSlateLightBorder
)

@Composable
fun CleanMinimalismTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CleanMinimalismColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun HiddenIndianTheme(
    content: @Composable () -> Unit
) {
    CleanMinimalismTheme(content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    CleanMinimalismTheme(content = content)
}

