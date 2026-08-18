package com.dating.home.presentation.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Módulo 4 — 48h "make a plan" countdown (RN-4.1). Red when < 12h remain.
 * Frozen states show a confirmation pill; expired shows "Expirado".
 */
@Composable
fun MatchTimer(
    expiresAt: String?,
    state: String,
    modifier: Modifier = Modifier
) {
    when (state) {
        "PLAN_CONFIRMED", "MET" -> {
            Pill("Plan confirmado ✓", Color(0xFF2E7D32), modifier); return
        }
        "PLAN_PROPOSED" -> {
            Pill("Plan propuesto", Color(0xFF1565C0), modifier); return
        }
        "EXPIRED" -> {
            Pill("Expirado", Color(0xFF757575), modifier); return
        }
    }
    if (expiresAt.isNullOrBlank()) return
    val expiry = remember(expiresAt) { runCatching { Instant.parse(expiresAt) }.getOrNull() } ?: return

    var now by remember { mutableStateOf(Clock.System.now()) }
    LaunchedEffect(expiresAt) {
        while (true) {
            now = Clock.System.now()
            delay(60_000)
        }
    }

    val remaining = expiry - now
    if (remaining.isNegative()) {
        Pill("Expirado", Color(0xFF757575), modifier); return
    }
    val hours = remaining.inWholeHours
    val minutes = remaining.inWholeMinutes % 60
    val urgent = hours < 12
    Pill(
        text = "⏳ ${hours}h ${minutes}m",
        color = if (urgent) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
private fun Pill(text: String, color: Color, modifier: Modifier) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
