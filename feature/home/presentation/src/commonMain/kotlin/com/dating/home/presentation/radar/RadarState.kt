package com.dating.home.presentation.radar

import com.dating.core.presentation.util.UiText
import com.dating.home.domain.radar.RadarProfile
import com.dating.home.domain.radar.RadarSession
import com.dating.home.domain.radar.RadarZone

data class RadarState(
    val isLoading: Boolean = false,
    // Zone picker (after resolving nearby zones).
    val nearbyZones: List<RadarZone> = emptyList(),
    val selectedDurationMinutes: Long = 60,
    // Active broadcast.
    val activeSession: RadarSession? = null,
    val feed: List<RadarProfile> = emptyList(),
    val error: UiText? = null,
    val notEligible: Boolean = false,
    val rateLimited: Boolean = false,
    val noLocation: Boolean = false
) {
    val isBroadcasting: Boolean get() = activeSession != null
}

sealed interface RadarAction {
    // The ViewModel resolves the current coordinates from the LocationProvider (used only to
    // resolve the zone; never stored). The Root requests LOCATION permission before dispatching.
    data object OnFindZonesRequested : RadarAction
    data class OnDurationSelected(val minutes: Long) : RadarAction
    data class OnZoneSelected(val zoneId: String) : RadarAction
    data object OnRefreshFeed : RadarAction
    data object OnVerifyZone : RadarAction   // re-chequea si sigo en mi zona (RN-5 left_zone)
    data object OnPanic : RadarAction
    data object OnStopRadar : RadarAction
    data class OnLike(val userId: String) : RadarAction
    data class OnPass(val userId: String) : RadarAction
    data class OnSaveMatch(val matchId: String) : RadarAction
}

sealed interface RadarEvent {
    data class OnMatch(val userId: String) : RadarEvent
    data class OnError(val message: UiText) : RadarEvent
}
