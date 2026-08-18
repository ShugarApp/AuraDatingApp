package com.dating.home.presentation.chat.chat_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dating.core.domain.auth.Intention

/**
 * Banner fijo con las reglas de la categoría del match (Módulo 2, RN-2.2). La asimetría:
 * en `casual` el contenido consensuado NO es violación de intención; en `serious`/`friendship`
 * el contenido sexual no solicitado sí lo es.
 */
@Composable
fun ChatRulesBanner(
    intentionCode: String?,
    modifier: Modifier = Modifier
) {
    if (intentionCode == null) return
    val text = when (Intention.fromCode(intentionCode)) {
        Intention.CASUAL ->
            "Match casual: el contenido consensuado no es reportable como violación de intención (sí como acoso)."
        Intention.SERIOUS, Intention.FRIENDSHIP ->
            "En este match, el contenido sexual no solicitado puede reportarse como violación de intención."
        Intention.OPEN ->
            "Sé respetuoso. Puedes reportar cualquier mensaje con un long-press."
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
