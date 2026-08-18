package com.dating.home.presentation.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import aura.feature.home.presentation.generated.resources.Res
import aura.feature.home.presentation.generated.resources.safety_check_desc
import aura.feature.home.presentation.generated.resources.safety_check_ok
import aura.feature.home.presentation.generated.resources.safety_check_report
import aura.feature.home.presentation.generated.resources.safety_check_title
import com.dating.core.designsystem.components.buttons.AppButtonStyle
import com.dating.core.designsystem.components.buttons.ChirpButton
import com.dating.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource

/**
 * Módulo 4 (RN-4.8) — se muestra 12h después de una cita agendada. "Reportar algo" abre
 * el flujo de Módulo 2 con categoría 'safety' en el servidor; "Todo bien" solo confirma.
 * No se puede descartar sin responder (una de las dos respuestas es obligatoria).
 */
@Composable
fun SafetyCheckDialog(
    otherUsername: String,
    onAnswer: (response: String) -> Unit
) {
    Dialog(
        onDismissRequest = { /* respuesta obligatoria */ },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .widthIn(max = 480.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.safety_check_title, otherUsername),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )
                Text(
                    text = stringResource(Res.string.safety_check_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
                ChirpButton(
                    text = stringResource(Res.string.safety_check_ok),
                    onClick = { onAnswer("ok") },
                    style = AppButtonStyle.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
                ChirpButton(
                    text = stringResource(Res.string.safety_check_report),
                    onClick = { onAnswer("report") },
                    style = AppButtonStyle.DESTRUCTIVE_PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
