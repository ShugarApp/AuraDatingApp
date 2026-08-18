package com.dating.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = AuraAccentStrong,
    destructiveHover = ChirpRed600,
    destructiveSecondaryOutline = ChirpRed200,
    disabledOutline = AuraLightBorder,
    disabledFill = AuraLightDisabledFill,
    successOutline = AuraSuccessSoft,
    success = AuraSuccess,
    onSuccess = AuraLightBg,
    secondaryFill = AuraLightSurfaceSubtle,

    textPrimary = AuraLightInk,
    textTertiary = AuraLightBg, // white — used as content color on filled primary button
    textSecondary = AuraLightTextSecondary,
    textPlaceholder = AuraLightTextTertiary,
    textDisabled = AuraLightDisabledText,

    surfaceLower = AuraLightSurfaceSubtle,
    surfaceHigher = AuraLightSurfaceSubtle,
    surfaceOutline = ChirpBase1000Alpha14,
    overlay = ChirpBase1000Alpha80,

    accentBlue = ChirpBlue,
    accentPurple = ChirpPurple,
    accentViolet = ChirpViolet,
    accentPink = ChirpPink,
    accentOrange = ChirpOrange,
    accentYellow = ChirpYellow,
    accentGreen = ChirpGreen,
    accentTeal = ChirpTeal,
    accentLightBlue = ChirpLightBlue,
    accentGrey = ChirpGrey,

    cakeViolet = ChirpCakeLightViolet,
    cakeGreen = ChirpCakeLightGreen,
    cakeBlue = ChirpCakeLightBlue,
    cakePink = ChirpCakeLightPink,
    cakeOrange = ChirpCakeLightOrange,
    cakeYellow = ChirpCakeLightYellow,
    cakeTeal = ChirpCakeLightTeal,
    cakePurple = ChirpCakeLightPurple,
    cakeRed = ChirpCakeLightRed,
    cakeMint = ChirpCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = AuraAccentStrong,
    destructiveHover = ChirpRed600,
    destructiveSecondaryOutline = ChirpRed200,
    disabledOutline = AuraDarkBorder,
    disabledFill = AuraDarkDisabledFill,
    successOutline = Color(0xFF14532D),
    success = AuraSuccess,
    onSuccess = AuraDarkBg,
    secondaryFill = AuraDarkSurfaceSubtle,

    textPrimary = AuraDarkInk,
    textTertiary = AuraLightBg, // white — content color on filled primary button
    textSecondary = AuraDarkTextSecondary,
    textPlaceholder = AuraDarkTextTertiary,
    textDisabled = AuraDarkDisabledText,

    surfaceLower = AuraDarkBg,
    surfaceHigher = AuraDarkSurface,
    surfaceOutline = ChirpBase100Alpha10,
    overlay = ChirpBase1000Alpha80,

    accentBlue = ChirpBlue,
    accentPurple = ChirpPurple,
    accentViolet = ChirpViolet,
    accentPink = ChirpPink,
    accentOrange = ChirpOrange,
    accentYellow = ChirpYellow,
    accentGreen = ChirpGreen,
    accentTeal = ChirpTeal,
    accentLightBlue = ChirpLightBlue,
    accentGrey = ChirpGrey,

    cakeViolet = ChirpCakeDarkViolet,
    cakeGreen = ChirpCakeDarkGreen,
    cakeBlue = ChirpCakeDarkBlue,
    cakePink = ChirpCakeDarkPink,
    cakeOrange = ChirpCakeDarkOrange,
    cakeYellow = ChirpCakeDarkYellow,
    cakeTeal = ChirpCakeDarkTeal,
    cakePurple = ChirpCakeDarkPurple,
    cakeRed = ChirpCakeDarkRed,
    cakeMint = ChirpCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = AuraAccent,
    onPrimary = AuraLightBg,
    primaryContainer = AuraAccentSoft,
    onPrimaryContainer = AuraAccentStrong,

    secondary = AuraLightTextSecondary,
    onSecondary = AuraLightBg,
    secondaryContainer = AuraLightSurfaceSubtle,
    onSecondaryContainer = AuraLightInk,

    tertiary = AuraAccentStrong,
    onTertiary = AuraLightBg,
    tertiaryContainer = AuraAccentSoft,
    onTertiaryContainer = AuraAccentStrong,

    error = AuraDestructive,
    onError = AuraLightBg,
    errorContainer = AuraDestructiveSoft,
    onErrorContainer = ChirpRed600,

    background = AuraLightBg,
    onBackground = AuraLightInk,
    surface = AuraLightSurface,
    onSurface = AuraLightInk,
    surfaceVariant = AuraLightSurfaceSubtle,
    onSurfaceVariant = AuraLightTextSecondary,

    outline = AuraLightBorder,
    outlineVariant = AuraLightBorder,
)

val DarkColorScheme = darkColorScheme(
    primary = AuraAccent,
    onPrimary = AuraLightBg,
    primaryContainer = AuraAccentSoftDark,
    onPrimaryContainer = AuraAccent,

    secondary = AuraDarkTextSecondary,
    onSecondary = AuraDarkBg,
    secondaryContainer = AuraDarkSurfaceSubtle,
    onSecondaryContainer = AuraDarkInk,

    tertiary = AuraAccent,
    onTertiary = AuraDarkBg,
    tertiaryContainer = AuraAccentSoftDark,
    onTertiaryContainer = AuraAccent,

    error = AuraDestructive,
    onError = AuraLightBg,
    errorContainer = Color(0xFF4A1122),
    onErrorContainer = ChirpRed200,

    background = AuraDarkBg,
    onBackground = AuraDarkInk,
    surface = AuraDarkSurface,
    onSurface = AuraDarkInk,
    surfaceVariant = AuraDarkSurfaceSubtle,
    onSurfaceVariant = AuraDarkTextSecondary,

    outline = AuraDarkBorder,
    outlineVariant = AuraDarkBorder,
)
