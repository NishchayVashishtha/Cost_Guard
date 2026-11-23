package com.costguard.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ForestGreen = Color(0xFF176A50)
val DarkForest = Color(0xFF0F4433)
val AccentSky = Color(0xFF0284C7)
val AccentPurple = Color(0xFF8B5CF6)
val AccentRose = Color(0xFFE11D48)
val BackgroundLight = Color(0xFFF8FAF8)
val SurfaceCard = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    secondary = AccentSky,
    tertiary = AccentPurple,
    background = BackgroundLight,
    surface = SurfaceCard,
    error = AccentRose
)

@Composable
fun CostGuardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
