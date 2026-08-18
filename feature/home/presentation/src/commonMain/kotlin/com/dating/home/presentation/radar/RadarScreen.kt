package com.dating.home.presentation.radar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dating.core.designsystem.components.buttons.AppButtonStyle
import com.dating.core.designsystem.components.buttons.ChirpButton
import com.dating.core.designsystem.components.chips.ChirpChip
import com.dating.home.domain.radar.RadarProfile

/**
 * Radar / Modo Aura (Módulo 5). Shows only the shared zone ("Está en Zona X") — never a map
 * or distance in meters. Always time-boxed, with a panic button while broadcasting.
 */
@Composable
fun RadarScreen(
    state: RadarState,
    onAction: (RadarAction) -> Unit,
    onFindZones: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Radar · Modo Aura",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        when {
            state.notEligible -> Info("Solo cuentas verificadas y sin sanciones activas pueden usar el radar.")
            state.rateLimited -> Info("Alcanzaste el límite de 3 sesiones de radar por hoy.")
            state.noLocation -> Info("No pudimos obtener tu ubicación. Activa el GPS y concede el permiso de ubicación.")
        }

        if (!state.isBroadcasting) {
            IdleContent(state, onAction, onFindZones)
        } else {
            BroadcastingContent(state, onAction)
        }
    }
}

@Composable
private fun IdleContent(state: RadarState, onAction: (RadarAction) -> Unit, onFindZones: () -> Unit) {
    Text(
        text = "Enciende el radar para ver a quién está cerca ahora mismo. Se apaga solo.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    SectionLabel("Duración")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(60L to "1 h", 120L to "2 h", 240L to "4 h").forEach { (mins, label) ->
            ChirpChip(
                text = label,
                isSelected = state.selectedDurationMinutes == mins,
                onClick = { onAction(RadarAction.OnDurationSelected(mins)) }
            )
        }
    }

    if (state.nearbyZones.isEmpty()) {
        ChirpButton(
            text = "Buscar mi zona",
            onClick = onFindZones,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        SectionLabel("Elige tu zona")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.nearbyZones) { zone ->
                RadarCard(onClick = { onAction(RadarAction.OnZoneSelected(zone.id)) }) {
                    Text(
                        text = zone.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (zone.kind == "event") "Evento" else "Zona permanente",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun BroadcastingContent(state: RadarState, onAction: (RadarAction) -> Unit) {
    Text(
        text = "Estás visible en ${state.activeSession?.zoneName ?: "tu zona"}",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ChirpButton(
            text = "🛑 Pánico",
            onClick = { onAction(RadarAction.OnPanic) },
            style = AppButtonStyle.DESTRUCTIVE_PRIMARY,
            modifier = Modifier.weight(1f)
        )
        ChirpButton(
            text = "Detener",
            onClick = { onAction(RadarAction.OnStopRadar) },
            style = AppButtonStyle.SECONDARY,
            modifier = Modifier.weight(1f)
        )
    }

    if (state.feed.isEmpty()) {
        Info("Nadie más con el radar encendido en tu zona ahora. Vuelve a mirar en un rato.")
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.feed) { profile -> RadarProfileCard(profile, onAction) }
        }
    }
}

@Composable
private fun RadarProfileCard(profile: RadarProfile, onAction: (RadarAction) -> Unit) {
    RadarCard {
        Text(
            text = profile.user.username + (profile.user.age()?.let { ", $it" } ?: ""),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Está en ${profile.sharedZone}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 4.dp)) {
            ChirpButton(
                text = "Pasar",
                onClick = { onAction(RadarAction.OnPass(profile.user.id)) },
                style = AppButtonStyle.SECONDARY,
                modifier = Modifier.weight(1f)
            )
            ChirpButton(
                text = "Me interesa",
                onClick = { onAction(RadarAction.OnLike(profile.user.id)) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RadarCard(
    onClick: (() -> Unit)? = null,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
    val border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    if (onClick != null) {
        Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = shape, colors = colors, border = border) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp), content = content)
        }
    } else {
        Card(modifier = Modifier.fillMaxWidth(), shape = shape, colors = colors, border = border) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp), content = content)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun Info(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/** Age is derived from birthDate on the client already elsewhere; keep radar cards light. */
private fun com.dating.core.domain.auth.User.age(): Int? = null
