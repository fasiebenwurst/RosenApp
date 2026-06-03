package de.empirius.rosenapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Rose,
    onPrimary = Color.White,
    primaryContainer = RoseLight,
    onPrimaryContainer = RoseDark,
    secondary = Leaf,
    onSecondary = Color.White,
    secondaryContainer = LeafLight,
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
)

private val DarkColors = darkColorScheme(
    primary = RoseLight,
    onPrimary = RoseDark,
    primaryContainer = RoseDark,
    onPrimaryContainer = RoseLight,
    secondary = LeafLight,
    background = Color(0xFF1A1115),
    onBackground = Cream,
    surface = Color(0xFF241A1E),
    onSurface = Cream,
)

@Composable
fun RosenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = RosenTypography,
        content = content,
    )
}
