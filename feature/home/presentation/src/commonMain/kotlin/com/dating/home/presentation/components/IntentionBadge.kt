package com.dating.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dating.core.domain.auth.Intention

/**
 * Small pill showing a user's declared intention (Módulo 1, RN-1.5). Shown on the
 * discovery card, profile, matches list and chat header — always visible.
 */
@Composable
fun IntentionBadge(
    intention: Intention,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = intentionColors(intention)
    Text(
        text = intention.displayName,
        color = fg,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

@Composable
fun IntentionBadge(
    intentionCode: String?,
    modifier: Modifier = Modifier
) = IntentionBadge(Intention.fromCode(intentionCode), modifier)

private fun intentionColors(intention: Intention): Pair<Color, Color> = when (intention) {
    Intention.SERIOUS -> Color(0xFFE53935) to Color.White        // red — commitment
    Intention.CASUAL -> Color(0xFFFB8C00) to Color.White         // orange — casual
    Intention.FRIENDSHIP -> Color(0xFF43A047) to Color.White     // green — friendship
    Intention.OPEN -> Color(0xFF1E88E5) to Color.White           // blue — open
}
