package com.dating.home.presentation.radar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dating.home.domain.radar.RadarProfile

/**
 * Radar / Modo Aura (Módulo 5). Shows only the shared zone ("Está en Zona X") — never a map
 * or distance in meters. Always time-boxed, with a panic button while broadcasting.
 *
 * [onFindZones] is provided by the host, which supplies the current coordinates from a
 * location source (used only to resolve the zone; never stored).
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Radar · Modo Aura", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

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
    Text("Enciende el radar para ver a quién está cerca ahora mismo. Se apaga solo.",
        color = MaterialTheme.colorScheme.onSurfaceVariant)

    Text("Duración", fontWeight = FontWeight.SemiBold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(60L to "1 h", 120L to "2 h", 240L to "4 h").forEach { (mins, label) ->
            FilterChip(
                selected = state.selectedDurationMinutes == mins,
                onClick = { onAction(RadarAction.OnDurationSelected(mins)) },
                label = { Text(label) }
            )
        }
    }

    if (state.nearbyZones.isEmpty()) {
        Button(onClick = onFindZones, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar mi zona")
        }
    } else {
        Text("Elige tu zona", fontWeight = FontWeight.SemiBold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.nearbyZones) { zone ->
                Card(
                    onClick = { onAction(RadarAction.OnZoneSelected(zone.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(zone.name, fontWeight = FontWeight.Bold)
                        Text(if (zone.kind == "event") "Evento" else "Zona permanente",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun BroadcastingContent(state: RadarState, onAction: (RadarAction) -> Unit) {
    Text("Estás visible en ${state.activeSession?.zoneName ?: "tu zona"}",
        fontWeight = FontWeight.SemiBold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onAction(RadarAction.OnPanic) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            modifier = Modifier.weight(1f)
        ) { Text("🛑 Pánico") }
        OutlinedButton(
            onClick = { onAction(RadarAction.OnStopRadar) },
            modifier = Modifier.weight(1f)
        ) { Text("Detener") }
    }

    if (state.feed.isEmpty()) {
        Info("Nadie más con el radar encendido en tu zona ahora. Vuelve a mirar en un rato.")
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.feed) { profile -> RadarProfileCard(profile, onAction) }
        }
    }
}

@Composable
private fun RadarProfileCard(profile: RadarProfile, onAction: (RadarAction) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = profile.user.username + (profile.user.age()?.let { ", $it" } ?: ""),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Está en ${profile.sharedZone}", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { onAction(RadarAction.OnPass(profile.user.id)) },
                    modifier = Modifier.weight(1f)
                ) { Text("Pasar") }
                Button(
                    onClick = { onAction(RadarAction.OnLike(profile.user.id)) },
                    modifier = Modifier.weight(1f)
                ) { Text("Me interesa") }
            }
        }
    }
}

@Composable
private fun Info(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium)
    }
}

/** Age is derived from birthDate on the client already elsewhere; keep radar cards light. */
private fun com.dating.core.domain.auth.User.age(): Int? = null
