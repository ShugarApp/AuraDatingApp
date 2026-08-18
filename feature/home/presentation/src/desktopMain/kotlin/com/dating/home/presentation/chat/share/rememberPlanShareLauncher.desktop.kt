package com.dating.home.presentation.chat.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
actual fun rememberPlanShareLauncher(): (planText: String) -> Unit {
    return remember {
        { planText ->
            // Desktop no tiene share sheet nativo: copiamos el plan al portapapeles.
            runCatching {
                Toolkit.getDefaultToolkit().systemClipboard
                    .setContents(StringSelection(planText), null)
            }
        }
    }
}
