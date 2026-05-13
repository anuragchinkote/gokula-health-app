package com.gokulahealth.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    secondary = ClayBrown,
    tertiary = GoldHighlight,
    background = SoilBlack,
    surface = MossCard,
    onPrimary = SoilBlack,
    onSecondary = SoilBlack,
    onTertiary = SoilBlack,
    onBackground = WarmWhiteText,
    onSurface = WarmWhiteText,
    surfaceVariant = OliveShadow,
    onSurfaceVariant = WarmWhiteText
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = EarthBrown,
    tertiary = HayAccent,
    background = IvoryBack,
    surface = SageSurface,
    onPrimary = MilkCream,
    onSecondary = MilkCream,
    onTertiary = CocoaText,
    onBackground = CocoaText,
    onSurface = CocoaText,
    surfaceVariant = MilkCream,
    onSurfaceVariant = CocoaText,
    error = AgriError
)

@Composable
fun GokulaHealthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GokulaTypography,
        shapes = GokulaShapes,
        content = content
    )
}
