package com.buildxide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = AccentBlueHover,
    secondary = AccentGreen,
    onSecondary = Color.White,
    secondaryContainer = AccentGreen.copy(alpha = 0.2f),
    onSecondaryContainer = AccentGreen,
    tertiary = AccentPurple,
    onTertiary = Color.White,
    tertiaryContainer = AccentPurple.copy(alpha = 0.2f),
    onTertiaryContainer = AccentPurple,
    error = AccentRed,
    onError = Color.White,
    errorContainer = AccentRed.copy(alpha = 0.2f),
    onErrorContainer = AccentRed,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Border,
    outlineVariant = Border.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = TextPrimary,
    inverseOnSurface = Background,
    inversePrimary = AccentBlueHover
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0969DA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0969DA).copy(alpha = 0.1f),
    onPrimaryContainer = Color(0xFF0969DA),
    secondary = Color(0xFF1A7F37),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1A7F37).copy(alpha = 0.1f),
    onSecondaryContainer = Color(0xFF1A7F37),
    tertiary = Color(0xFF8250DF),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF8250DF).copy(alpha = 0.1f),
    onTertiaryContainer = Color(0xFF8250DF),
    error = Color(0xFFCF222E),
    onError = Color.White,
    errorContainer = Color(0xFFCF222E).copy(alpha = 0.1f),
    onErrorContainer = Color(0xFFCF222E),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F2328),
    surface = Color(0xFFF6F8FA),
    onSurface = Color(0xFF1F2328),
    surfaceVariant = Color(0xFFEAEEF2),
    onSurfaceVariant = Color(0xFF656D76),
    outline = Color(0xFFD0D7DE),
    outlineVariant = Color(0xFFD0D7DE).copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = Color(0xFF1F2328),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF0969DA)
)

val AmoledColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = AccentBlueHover,
    secondary = AccentGreen,
    onSecondary = Color.White,
    secondaryContainer = AccentGreen.copy(alpha = 0.2f),
    onSecondaryContainer = AccentGreen,
    tertiary = AccentPurple,
    onTertiary = Color.White,
    tertiaryContainer = AccentPurple.copy(alpha = 0.2f),
    onTertiaryContainer = AccentPurple,
    error = AccentRed,
    onError = Color.White,
    errorContainer = AccentRed.copy(alpha = 0.2f),
    onErrorContainer = AccentRed,
    background = Color.Black,
    onBackground = TextPrimary,
    surface = Color(0xFF0A0A0A),
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF111111),
    onSurfaceVariant = TextSecondary,
    outline = Border,
    outlineVariant = Border.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.7f),
    inverseSurface = TextPrimary,
    inverseOnSurface = Color.Black,
    inversePrimary = AccentBlueHover
)

enum class AppTheme {
    LIGHT, DARK, AMOLED
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme.DARK }

@Composable
fun BuildxIDETheme(
    theme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.AMOLED -> AmoledColorScheme
    }

    CompositionLocalProvider(LocalAppTheme provides theme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun BuildxIDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    BuildxIDETheme(
        theme = if (darkTheme) AppTheme.DARK else AppTheme.LIGHT,
        content = content
    )
}
