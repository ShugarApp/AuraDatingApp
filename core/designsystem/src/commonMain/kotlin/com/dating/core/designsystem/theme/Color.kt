package com.dating.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/* =====================================================================
 *  AURA — "Soft Minimal" palette
 *  Light: white bg, lilac accent (#A78BFA), warm-neutral ink.
 *  Dark: near-black neutral bg, same lilac accent, inverted text.
 *  The legacy Chirp* tokens are re-pointed to this palette so every
 *  existing reference keeps working with the new look.
 * ===================================================================== */

// --- Semantic Aura tokens (preferred going forward) ---
val AuraAccent = Color(0xFFA78BFA)        // primary lilac / CTA
val AuraAccentStrong = Color(0xFF8B5CF6)  // pressed / strong accent
val AuraAccentSoft = Color(0xFFF1ECFE)    // accent tint bg (light)
val AuraAccentSoftDark = Color(0xFF241E38) // accent tint bg (dark)
val AuraAccentAlpha40 = Color(0x66A78BFA)

val AuraDestructive = Color(0xFFF43F5E)
val AuraDestructiveSoft = Color(0xFFFDE7EB)
val AuraSuccess = Color(0xFF22C55E)
val AuraSuccessSoft = Color(0xFFDCFCE7)
val AuraVerified = Color(0xFF38BDF8)

// Intention accents
val AuraIntentSerious = Color(0xFFF43F5E)
val AuraIntentCasual = Color(0xFFF59E0B)
val AuraIntentFriendship = Color(0xFF22C55E)
val AuraIntentOpen = Color(0xFF3B82F6)

// Light neutrals
val AuraLightBg = Color(0xFFFFFFFF)
val AuraLightSurface = Color(0xFFFFFFFF)
val AuraLightSurfaceSubtle = Color(0xFFF7F7F9)
val AuraLightBorder = Color(0xFFECECEF)
val AuraLightInk = Color(0xFF1A1A22)
val AuraLightTextSecondary = Color(0xFF6B6B76)
val AuraLightTextTertiary = Color(0xFFA0A0AB)
val AuraLightDisabledFill = Color(0xFFF0EEF6)
val AuraLightDisabledText = Color(0xFFB7AFCE)

// Dark neutrals
val AuraDarkBg = Color(0xFF0F0D14)
val AuraDarkSurface = Color(0xFF17151F)
val AuraDarkSurfaceSubtle = Color(0xFF1E1B29)
val AuraDarkBorder = Color(0xFF2C2938)
val AuraDarkInk = Color(0xFFF5F4F8)
val AuraDarkTextSecondary = Color(0xFFAAA6B8)
val AuraDarkTextTertiary = Color(0xFF716D80)
val AuraDarkDisabledFill = Color(0xFF221F2E)
val AuraDarkDisabledText = Color(0xFF5A566A)

/* --- Legacy Chirp* tokens, re-pointed to the Soft Minimal palette --- */

// Brand (now lilac)
val ChirpBrand1000 = AuraAccentSoft
val ChirpBrand900 = Color(0xFF2B1B45)
val ChirpBrand600 = AuraAccentStrong
val ChirpBrand500 = AuraAccent
val ChirpBrand500Alpha40 = AuraAccentAlpha40
val ChirpBrand100 = AuraAccentSoft

// Base (neutral scale, light → dark)
val ChirpBase1000 = AuraDarkBg
val ChirpBase1000Alpha8 = Color(0x141A1A22)
val ChirpBase1000Alpha80 = Color(0xCC0F0D14)
val ChirpBase1000Alpha14 = Color(0x241A1A22)
val ChirpBase950 = AuraDarkSurface
val ChirpBase900 = AuraLightInk
val ChirpBase800 = Color(0xFF33333B)
val ChirpBase700 = Color(0xFF55555F)
val ChirpBase500 = AuraLightTextSecondary
val ChirpBase400 = AuraLightTextTertiary
val ChirpBase200 = Color(0xFFE4E4E8)
val ChirpBase150 = AuraLightBorder
val ChirpBase100 = AuraLightSurfaceSubtle
val ChirpBase100Alpha10 = Color(0x1AF7F7F9)
val ChirpBase0 = Color(0xFFFFFFFF)

// Red / destructive
val ChirpRed600 = Color(0xFFD91E45)
val ChirpRed500 = AuraDestructive
val ChirpRed200 = Color(0xFFFECDD6)

// Accent Colors (soft translucent, used for multi-color UI variety)
val ChirpBlue = AuraVerified
val ChirpBlue2 = Color(0xFF7FCBFF)
val ChirpBlue3 = Color(0xFF7FCBFF)

val ChirpPurple = Color(0x26A78BFA)
val ChirpViolet = Color(0x26C77DFF)
val ChirpPink = Color(0x26FF8FB1)
val ChirpOrange = Color(0x26FFB37A)
val ChirpYellow = Color(0x26FFE89A)
val ChirpGreen = Color(0x2686FFC2)
val ChirpTeal = Color(0x2674FFE5)
val ChirpLightBlue = Color(0x268EC5FF)
val ChirpGrey = Color(0x26D9D2E5)

// Cake Colors - Light Theme (soft pastels for chat bubbles)
val ChirpCakeLightViolet = Color(0xFFF1ECFE)
val ChirpCakeLightGreen = Color(0xFFE6F9EF)
val ChirpCakeLightBlue = Color(0xFFE8F1FF)
val ChirpCakeLightPink = Color(0xFFFFE8F0)
val ChirpCakeLightOrange = Color(0xFFFFF0E4)
val ChirpCakeLightYellow = Color(0xFFFFFAE2)
val ChirpCakeLightTeal = Color(0xFFE2FBF6)
val ChirpCakeLightPurple = Color(0xFFEDE7FE)
val ChirpCakeLightRed = Color(0xFFFFE6EA)
val ChirpCakeLightMint = Color(0xFFE4FBF0)

// Cake Colors - Dark Theme
val ChirpCakeDarkViolet = Color(0x33A78BFA)
val ChirpCakeDarkGreen = Color(0x3386FFC2)
val ChirpCakeDarkBlue = Color(0x334F9DFF)
val ChirpCakeDarkPink = Color(0x33FF8FB1)
val ChirpCakeDarkOrange = Color(0x33FFB37A)
val ChirpCakeDarkYellow = Color(0x33FFE89A)
val ChirpCakeDarkTeal = Color(0x3374FFE5)
val ChirpCakeDarkPurple = Color(0x33C77DFF)
val ChirpCakeDarkRed = Color(0x33FF6B8A)
val ChirpCakeDarkMint = Color(0x3386FFD4)
