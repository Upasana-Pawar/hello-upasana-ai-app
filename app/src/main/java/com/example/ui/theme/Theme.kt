package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TokyoColorScheme = darkColorScheme(
    primary = TokyoBlue,
    secondary = TokyoPurple,
    tertiary = TokyoTeal,
    background = TokyoBg,
    surface = TokyoCard,
    onPrimary = TokyoBg,
    onSecondary = TokyoBg,
    onTertiary = TokyoBg,
    onBackground = TokyoTextPrimary,
    onSurface = TokyoTextPrimary,
    outline = TokyoBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to Tokyo-Night theme
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = TokyoColorScheme,
        typography = Typography,
        content = content
    )
}
