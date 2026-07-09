package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SleekPurple,
    secondary = SleekCardGradStart,
    tertiary = SleekPurpleDark,
    background = SleekBg,
    surface = SleekRewardBg,
    onPrimary = PureWhite,
    onSecondary = SleekPurpleDark,
    onTertiary = PureWhite,
    onBackground = SleekTextPrimary,
    onSurface = SleekTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = SleekPurple,
    secondary = SleekCardGradStart,
    tertiary = SleekPurpleDark,
    background = SleekBg,
    surface = SleekRewardBg,
    onPrimary = PureWhite,
    onSecondary = SleekPurpleDark,
    onTertiary = PureWhite,
    onBackground = SleekTextPrimary,
    onSurface = SleekTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Set false to default to Sleek Interface light style
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
