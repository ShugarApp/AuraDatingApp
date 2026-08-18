package com.dating.home.presentation.chat.share

import androidx.compose.runtime.Composable

/**
 * Módulo 4 (RN-4.7) — abre el share sheet nativo para compartir un plan de cita con un contacto
 * de confianza. El texto lo construye quien invoca y NUNCA incluye teléfono ni apellido del match.
 * Devuelve una función que dispara el share con el texto dado.
 */
@Composable
expect fun rememberPlanShareLauncher(): (planText: String) -> Unit
