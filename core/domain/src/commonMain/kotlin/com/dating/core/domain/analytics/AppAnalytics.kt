package com.dating.core.domain.analytics

/**
 * Abstracción de analítica accesible desde los ViewModels (Requisito transversal). Emite los
 * eventos diferenciadores. La implementación real (Firebase) se enlaza en composeApp; por
 * defecto se usa [NoOpAppAnalytics].
 */
interface AppAnalytics {
    fun track(event: String, params: Map<String, String> = emptyMap())

    object Events {
        const val INTENTION_CHANGED = "intention_changed"
        const val LIKE_WITH_NOTE = "like_with_note"
        const val DECK_EXHAUSTED = "deck_exhausted"
        const val PLAN_PROPOSED = "plan_proposed"
        const val PLAN_CONFIRMED = "plan_confirmed"
        const val REPORT_CREATED = "report_created"
        const val MATCH_REVIVED = "match_revived"
        const val RADAR_SESSION_STARTED = "radar_session_started"
        const val RADAR_MATCH_CREATED = "radar_match_created"
        const val RADAR_MATCH_SAVED = "radar_match_saved"
        const val RADAR_PANIC_PRESSED = "radar_panic_pressed"
    }
}

/** Implementación por defecto sin efectos (se sobre-escribe en composeApp con Firebase). */
class NoOpAppAnalytics : AppAnalytics {
    override fun track(event: String, params: Map<String, String>) = Unit
}
