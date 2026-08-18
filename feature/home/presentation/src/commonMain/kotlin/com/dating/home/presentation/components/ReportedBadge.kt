package com.dating.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Clock
import kotlin.time.Instant

/** True while the public "reported" flag window is still open (RN-2.4). */
fun isPublicFlagActive(publicFlagUntil: String?): Boolean {
    if (publicFlagUntil.isNullOrBlank()) return false
    return try {
        Instant.parse(publicFlagUntil) > Clock.System.now()
    } catch (_: Exception) {
        false
    }
}

/**
 * Public "⚠ Reportado" badge shown on a user's card for 30 days after a strike 2 (RN-2.4).
 * Renders nothing when the flag window is not active.
 */
@Composable
fun ReportedBadge(
    publicFlagUntil: String?,
    modifier: Modifier = Modifier
) {
    if (!isPublicFlagActive(publicFlagUntil)) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF8E24AA))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "⚠ Reportado",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
