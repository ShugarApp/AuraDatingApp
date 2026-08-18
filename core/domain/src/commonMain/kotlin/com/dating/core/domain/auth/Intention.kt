package com.dating.core.domain.auth

/**
 * Canonical declared intention (Módulo 1). The backend serializes these as lowercase
 * strings ("serious" | "casual" | "friendship" | "open"); the app keeps the raw string
 * on [User.intention] and uses this helper for display and compatibility.
 */
enum class Intention(val code: String, val displayName: String) {
    SERIOUS("serious", "Serious"),
    CASUAL("casual", "Casual"),
    FRIENDSHIP("friendship", "Friendship"),
    OPEN("open", "Open");

    /** Compatibility is symmetric (RN-1.2): same intention or at least one is OPEN. */
    fun isCompatibleWith(other: Intention): Boolean =
        this == other || this == OPEN || other == OPEN

    companion object {
        fun fromCode(value: String?): Intention =
            entries.find { it.code.equals(value, ignoreCase = true) || it.name.equals(value, ignoreCase = true) }
                ?: OPEN

        /** Bucket estructural de un valor "Looking For" (mismo mapeo que el backend, RN-1.3). */
        fun fromLookingFor(lookingFor: String?): Intention = when (lookingFor?.uppercase()) {
            "LONG_TERM", "SHORT_TERM" -> SERIOUS
            "CASUAL_DATES", "HOOKUP" -> CASUAL
            "FRIENDS" -> FRIENDSHIP
            else -> OPEN
        }
    }
}

/** Status of the user's ability to change intention, gated by the 14-day cooldown (RN-1.3). */
data class IntentionStatus(
    val intention: Intention,
    val canChange: Boolean,
    val nextAvailableAt: String?
)
