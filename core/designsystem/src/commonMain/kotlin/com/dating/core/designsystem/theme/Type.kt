package com.dating.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import aura.core.designsystem.generated.resources.Res
import aura.core.designsystem.generated.resources.inter_bold
import aura.core.designsystem.generated.resources.inter_medium
import aura.core.designsystem.generated.resources.inter_regular
import aura.core.designsystem.generated.resources.inter_semibold
import aura.core.designsystem.generated.resources.poppins_bold
import aura.core.designsystem.generated.resources.poppins_medium
import aura.core.designsystem.generated.resources.poppins_regular
import aura.core.designsystem.generated.resources.poppins_semibold
import org.jetbrains.compose.resources.Font

/**
 * "Soft Minimal" type system.
 * Poppins → display / headline / title (headings). Inter → body / label (UI & body copy).
 */
val Poppins @Composable get() = FontFamily(
    Font(resource = Res.font.poppins_regular, weight = FontWeight.Normal),
    Font(resource = Res.font.poppins_medium, weight = FontWeight.Medium),
    Font(resource = Res.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(resource = Res.font.poppins_bold, weight = FontWeight.Bold),
)

val Inter @Composable get() = FontFamily(
    Font(resource = Res.font.inter_regular, weight = FontWeight.Normal),
    Font(resource = Res.font.inter_medium, weight = FontWeight.Medium),
    Font(resource = Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(resource = Res.font.inter_bold, weight = FontWeight.Bold),
)

// Kept for backwards compatibility; now aliases the new display font.
val PlusJakartaSans @Composable get() = Poppins

val Typography.labelXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )

val Typography.titleXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val Typography @Composable get() = Typography(
    // Display — Poppins
    displayLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 46.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    ),
    // Headline — Poppins
    headlineLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    // Title — Poppins
    titleLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // Body — Inter
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // Label — Inter
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
)
