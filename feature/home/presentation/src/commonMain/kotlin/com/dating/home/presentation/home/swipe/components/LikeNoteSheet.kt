package com.dating.home.presentation.home.swipe.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

const val LIKE_NOTE_MIN_LENGTH = 40

/**
 * Módulo 3 — a like requires a note of at least 40 chars (RN-3.3). Shown as a bottom sheet
 * before the like is sent; the server also validates (422).
 */
@Composable
fun LikeNoteSheetContent(
    targetName: String?,
    isError: Boolean,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf("") }
    val length = note.trim().length
    val valid = length >= LIKE_NOTE_MIN_LENGTH

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = targetName?.let { "¿Por qué te interesa $it?" } ?: "¿Por qué te interesa?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Escribe una nota real (mínimo $LIKE_NOTE_MIN_LENGTH caracteres). La verá antes de decidir.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            placeholder = { Text("Algo específico de su perfil…") },
            isError = isError && !valid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
        )
        Text(
            text = "$length/$LIKE_NOTE_MIN_LENGTH",
            style = MaterialTheme.typography.labelMedium,
            color = if (valid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
        Button(
            onClick = { onSubmit(note) },
            enabled = valid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar like")
        }
    }
}
