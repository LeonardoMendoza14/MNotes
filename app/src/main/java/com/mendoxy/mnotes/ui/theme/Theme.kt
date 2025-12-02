package com.mendoxy.mnotes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.mendoxy.mnotes.ui.presentation.SettingsState
import androidx.compose.material3.Typography
import com.mendoxy.mnotes.ui.utils.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    secondary = secondaryDark,
    tertiary = tertiaryDark,
    background = backgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    outline = outlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    secondary = secondaryLight,
    tertiary = tertiaryLight,
    background = backgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    outline = outlineLight

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val BaseTypography = Typography

// Lógica de escalado
private fun Typography.applyScale(scaleFactor: Float): Typography {
    return this.copy(
        displayLarge = this.displayLarge.scale(scaleFactor),
        displayMedium = this.displayMedium.scale(scaleFactor),
        displaySmall = this.displaySmall.scale(scaleFactor),
        headlineLarge = this.headlineLarge.scale(scaleFactor),
        headlineMedium = this.headlineMedium.scale(scaleFactor),
        headlineSmall = this.headlineSmall.scale(scaleFactor),
        titleLarge = this.titleLarge.scale(scaleFactor),
        titleMedium = this.titleMedium.scale(scaleFactor),
        titleSmall = this.titleSmall.scale(scaleFactor),
        bodyLarge = this.bodyLarge.scale(scaleFactor),
        bodyMedium = this.bodyMedium.scale(scaleFactor),
        bodySmall = this.bodySmall.scale(scaleFactor),
        labelLarge = this.labelLarge.scale(scaleFactor),
        labelMedium = this.labelMedium.scale(scaleFactor),
        labelSmall = this.labelSmall.scale(scaleFactor)
    )
}

private fun TextStyle.scale(factor: Float): TextStyle {
    return this.copy(
        fontSize = this.fontSize * factor,
        lineHeight = this.lineHeight * factor
    )
}

@Composable
fun MNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    settings: SettingsState,
    content: @Composable () -> Unit
) {
    val colorScheme = when(settings.appTheme){
        AppTheme.DARK -> DarkColorScheme
        AppTheme.LIGHT -> LightColorScheme
        else -> DarkColorScheme
    }
    val newTypography = BaseTypography.applyScale(settings.fontSize.scaleFactor)

    MaterialTheme(
      colorScheme = colorScheme,
      typography = newTypography,
      content = content
    )
}