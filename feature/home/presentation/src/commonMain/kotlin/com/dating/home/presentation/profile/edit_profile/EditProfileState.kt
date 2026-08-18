package com.dating.home.presentation.profile.edit_profile

import androidx.compose.foundation.text.input.TextFieldState
import com.dating.core.presentation.util.UiText

data class EditProfileState(
    // Per-slot loading: which photo indices are being uploaded or deleted
    val uploadingSlots: Set<Int> = emptySet(),
    val deletingSlots: Set<Int> = emptySet(),
    val pendingDeleteSlot: Int? = null,
    val imageError: UiText? = null,

    val isSavingProfile: Boolean = false,
    val saveError: UiText? = null,
    val showSuccessMessage: Boolean = false,

    val bioError: String? = null,
    val birthDateError: String? = null,
    val heightError: String? = null,
    val interestsError: String? = null,

    // Read-only personal info (set at registration, not editable)
    val gender: String? = null,
    val birthDate: String? = null,

    // Editable profile fields
    val bioTextState: TextFieldState = TextFieldState(),
    val jobTitleTextState: TextFieldState = TextFieldState(),
    val companyTextState: TextFieldState = TextFieldState(),
    val educationTextState: TextFieldState = TextFieldState(),
    val height: Int? = null,
    val zodiac: String? = null,
    val smoking: String? = null,
    val drinking: String? = null,
    val selectedInterests: List<String> = emptyList(),
    val availableInterests: List<String> = listOf(
        "photography", "hiking", "music", "cooking", "travel",
        "fitness", "reading", "gaming", "art", "dancing",
        "yoga", "movies", "sports", "technology", "fashion",
        "food", "coffee", "pets", "nature", "volunteering"
    ),

    // Preferences (from registration, now editable)
    val interestedIn: String? = null,
    val lookingFor: String? = null,
    val idealDate: String? = null,

    // RN-1.3 — cooldown de intención (14 días). currentIntentionCode = bucket actual del servidor.
    val currentIntentionCode: String? = null,
    val intentionCanChange: Boolean = true,
    val intentionNextAvailableAt: String? = null,

    // 6 photo slots; null = empty
    val photos: List<String?> = List(6) { null }
) {
    val showDeleteConfirmationDialog: Boolean get() = pendingDeleteSlot != null

    /**
     * RN-1.3 — el "Looking For" seleccionado cruza a otro bucket de intención mientras el cooldown
     * está activo → el guardado devolverá 409. Sirve para avisar antes de intentar guardar.
     */
    val intentionChangeBlocked: Boolean
        get() = !intentionCanChange &&
            currentIntentionCode != null &&
            com.dating.core.domain.auth.Intention.fromLookingFor(lookingFor).code != currentIntentionCode
}
