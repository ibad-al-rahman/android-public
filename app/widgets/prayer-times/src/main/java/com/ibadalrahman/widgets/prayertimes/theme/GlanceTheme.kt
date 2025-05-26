package com.ibadalrahman.widgets.prayertimes.theme

import android.os.Build
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme
import androidx.glance.color.DayNightColorProvider
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle

object WidgetGlanceColorScheme {
    val primary = DayNightColorProvider(
        day = Color(0xFF1E398C),
        night = Color(0xFF0597B0)
    )

    val onPrimary = DayNightColorProvider(
        day = Color.White,
        night = Color.Black
    )

    val primaryContainer = DayNightColorProvider(
        day = Color(0xFFB2F0FA),
        night = Color(0xFF004E5C)
    )

    val onPrimaryContainer = DayNightColorProvider(
        day = Color(0xFF000000),
        night = Color.White
    )

    val secondary = DayNightColorProvider(
        day = Color(0xFF6E6E73),
        night = Color(0xFF636366)
    )

    val onSecondary = DayNightColorProvider(
        day = Color.White,
        night = Color.White
    )

    val secondaryContainer = DayNightColorProvider(
        day = Color(0xFFE5E5EA),
        night = Color(0xFF3A3A3C)
    )

    val onSecondaryContainer = DayNightColorProvider(
        day = Color(0xFF000000),
        night = Color.White
    )

    val tertiary = DayNightColorProvider(
        day = Color(0xFF5856D6),
        night = Color(0xFF5E5CE6)
    )

    val onTertiary = DayNightColorProvider(
        day = Color.White,
        night = Color.White
    )

    val error = DayNightColorProvider(
        day = Color(0xFFFF3B30),
        night = Color(0xFFFF453A)
    )

    val onError = DayNightColorProvider(
        day = Color.White,
        night = Color.Black
    )

    val background = DayNightColorProvider(
        day = Color(0xFFFFFFFF),
        night = Color(0xFF000000)
    )

    val onBackground = DayNightColorProvider(
        day = Color(0xFF000000),
        night = Color.White
    )

    val surface = DayNightColorProvider(
        day = Color(0xFFF2F2F7),
        night = Color(0xFF1C1C1E)
    )

    val onSurface = DayNightColorProvider(
        day = Color(0xFF000000),
        night = Color.White
    )

    val surfaceVariant = DayNightColorProvider(
        day = Color(0xFFD1D1D6),
        night = Color(0xFF2C2C2E)
    )

    val onSurfaceVariant = DayNightColorProvider(
        day = Color(0xAA3C3C43),
        night = Color(0xFFCACACA)
    )

    val outline = DayNightColorProvider(
        day = Color(0xFFC6C6C8),
        night = Color(0xFF3C3C43)
    )

    val inverseSurface = DayNightColorProvider(
        day = Color(0xFF1C1C1E),
        night = Color(0xFFE5E5EA)
    )

    val inverseOnSurface = DayNightColorProvider(
        day = Color.White,
        night = Color(0xFF1C1C1E)
    )

    val inversePrimary = DayNightColorProvider(
        day = Color(0xFFA0DFFF),
        night = Color(0xFF66BFFF)
    )
}

object WidgetGlanceTypography {
    val displayLarge = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold
    )

    val displayMedium = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium
    )

    val displaySmall = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    )

    val headlineLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )

    val headlineMedium = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )

    val headlineSmall = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal
    )

    val titleLarge = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )

    val titleMedium = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal
    )

    val titleSmall = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    )

    val bodyLarge = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal
    )

    val bodyMedium = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    )

    val bodySmall = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    )

    val labelLarge = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )

    val labelMedium = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )

    val labelSmall = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
@GlanceComposable
fun WidgetGlanceTheme(
    dynamicColors: Boolean = false,
    content: @Composable @GlanceComposable () -> Unit
) {
    val colors = when {
        dynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            GlanceTheme.colors
        }
        else -> {
            val lightColorScheme = lightColorScheme(
            primary = Color(0xFF1E398C),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFB2F0FA),
            onPrimaryContainer = Color(0xFF000000),
            secondary = Color(0xFF6E6E73),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE5E5EA),
            onSecondaryContainer = Color(0xFF000000),
            tertiary = Color(0xFF5856D6),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFF2F2F7),
            onTertiaryContainer = Color(0xFF000000),
            error = Color(0xFFFF3B30),
            onError = Color.White,
            errorContainer = Color(0xFFFFD1CC),
            onErrorContainer = Color(0xFF000000),
            background = Color(0xFFFFFFFF),
            onBackground = Color(0xFF000000),
            surface = Color(0xFFF2F2F7),
            onSurface = Color(0xFF000000),
            surfaceVariant = Color(0xFFD1D1D6),
            onSurfaceVariant = Color(0xAA3C3C43),
            outline = Color(0xFFC6C6C8),
            inverseSurface = Color(0xFF1C1C1E),
            inverseOnSurface = Color.White,
            inversePrimary = Color(0xFFA0DFFF)
        )

        val darkColorScheme = darkColorScheme(
            primary = Color(0xFF0597B0),
            onPrimary = Color.Black,
            primaryContainer = Color(0xFF004E5C),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFF636366),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFF3A3A3C),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFF5E5CE6),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFF000000),
            onTertiaryContainer = Color.White,
            error = Color(0xFFFF453A),
            onError = Color.Black,
            errorContainer = Color(0xFF5A0004),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF000000),
            onBackground = Color.White,
            surface = Color(0xFF1C1C1E),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF2C2C2E),
            onSurfaceVariant = Color(0xFFCACACA),
            outline = Color(0xFF3C3C43),
            inverseSurface = Color(0xFFE5E5EA),
            inverseOnSurface = Color(0xFF1C1C1E),
            inversePrimary = Color(0xFF66BFFF)
        )

            ColorProviders(light = lightColorScheme, dark = darkColorScheme)
        }
    }

    GlanceTheme(colors = colors) {
        content()
    }
}
