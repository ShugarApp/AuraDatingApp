package com.dating.home.presentation.chat.chat_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import aura.feature.home.presentation.generated.resources.Res
import aura.feature.home.presentation.generated.resources.system_close_match
import aura.feature.home.presentation.generated.resources.system_continue
import aura.feature.home.presentation.generated.resources.system_match_incompatible
import com.dating.home.presentation.chat.model.MessageUi
import org.jetbrains.compose.resources.stringResource

/**
 * RN-1.4 — burbuja centrada de mensaje de sistema. Para MATCH_INCOMPATIBLE ofrece continuar
 * (colapsa la tarjeta localmente) o cerrar el match (dispara el flujo de borrado existente).
 */
@Composable
fun SystemMessageBubble(
    message: MessageUi.SystemMessage,
    onCloseMatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (message.kind != "MATCH_INCOMPATIBLE") return

    var resolved by remember(message.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.system_match_incompatible),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        )
        if (!resolved) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { resolved = true }) {
                    Text(
                        text = stringResource(Res.string.system_continue),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                OutlinedButton(onClick = onCloseMatch) {
                    Text(
                        text = stringResource(Res.string.system_close_match),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
