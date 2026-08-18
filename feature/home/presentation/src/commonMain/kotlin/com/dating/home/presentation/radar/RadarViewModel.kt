package com.dating.home.presentation.radar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dating.core.domain.analytics.AppAnalytics
import com.dating.core.domain.location.LocationProvider
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.onFailure
import com.dating.core.domain.util.onSuccess
import com.dating.core.presentation.util.toUiText
import com.dating.home.domain.matching.SwipeAction
import com.dating.home.domain.radar.RadarService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RadarViewModel(
    private val radarService: RadarService,
    private val locationProvider: LocationProvider,
    private val analytics: AppAnalytics
) : ViewModel() {

    private val _state = MutableStateFlow(RadarState())
    val state = _state.asStateFlow()

    private val _events = Channel<RadarEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: RadarAction) {
        when (action) {
            RadarAction.OnFindZonesRequested -> findZones()
            is RadarAction.OnDurationSelected -> _state.update { it.copy(selectedDurationMinutes = action.minutes) }
            is RadarAction.OnZoneSelected -> activate(action.zoneId)
            RadarAction.OnRefreshFeed -> loadFeed()
            RadarAction.OnVerifyZone -> verifyStillInZone()
            RadarAction.OnPanic -> {
                analytics.track(AppAnalytics.Events.RADAR_PANIC_PRESSED)
                stop(reason = "panic")
            }
            RadarAction.OnStopRadar -> stop(reason = "manual")
            is RadarAction.OnLike -> swipe(action.userId, SwipeAction.LIKE)
            is RadarAction.OnPass -> swipe(action.userId, SwipeAction.DISLIKE)
            is RadarAction.OnSaveMatch -> saveMatch(action.matchId)
        }
    }

    private fun findZones() {
        _state.update { it.copy(isLoading = true, error = null, notEligible = false, rateLimited = false, noLocation = false) }
        viewModelScope.launch {
            val location = locationProvider.getLastKnownLocation()
            if (location == null) {
                _state.update { it.copy(isLoading = false, noLocation = true) }
                return@launch
            }
            radarService.nearbyZones(location.latitude, location.longitude)
                .onSuccess { zones -> _state.update { it.copy(isLoading = false, nearbyZones = zones) } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.toUiText()) } }
        }
    }

    private fun activate(zoneId: String) {
        _state.update { it.copy(isLoading = true, error = null, notEligible = false, rateLimited = false) }
        viewModelScope.launch {
            radarService.startSession(zoneId, _state.value.selectedDurationMinutes)
                .onSuccess { session ->
                    analytics.track(AppAnalytics.Events.RADAR_SESSION_STARTED)
                    _state.update { it.copy(isLoading = false, activeSession = session, nearbyZones = emptyList()) }
                    loadFeed()
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            notEligible = e == DataError.Remote.FORBIDDEN,
                            rateLimited = e == DataError.Remote.TOO_MANY_REQUESTS,
                            error = if (e == DataError.Remote.FORBIDDEN || e == DataError.Remote.TOO_MANY_REQUESTS) null else e.toUiText()
                        )
                    }
                }
        }
    }

    // RN-5 — si el usuario sale de la geocerca de su zona, termina la sesión (end_reason=left_zone).
    private fun verifyStillInZone() {
        val session = _state.value.activeSession ?: return
        viewModelScope.launch {
            val location = locationProvider.getLastKnownLocation() ?: return@launch
            radarService.nearbyZones(location.latitude, location.longitude).onSuccess { zones ->
                if (zones.none { it.id == session.zoneId }) {
                    radarService.endSession("left_zone")
                    _state.update { it.copy(activeSession = null, feed = emptyList(), nearbyZones = emptyList()) }
                }
            }
        }
    }

    private fun loadFeed() {
        if (_state.value.activeSession == null) return
        viewModelScope.launch {
            radarService.feed()
                .onSuccess { profiles -> _state.update { it.copy(feed = profiles) } }
                .onFailure { e -> _state.update { it.copy(error = e.toUiText()) } }
        }
    }

    private fun stop(reason: String) {
        viewModelScope.launch {
            radarService.endSession(reason)
            _state.update { it.copy(activeSession = null, feed = emptyList(), nearbyZones = emptyList()) }
        }
    }

    private fun swipe(userId: String, action: SwipeAction) {
        _state.update { it.copy(feed = it.feed.filter { p -> p.user.id != userId }) }
        viewModelScope.launch {
            radarService.swipe(userId, action)
                .onSuccess { isMatch ->
                    if (isMatch) {
                        analytics.track(AppAnalytics.Events.RADAR_MATCH_CREATED)
                        _events.send(RadarEvent.OnMatch(userId))
                    }
                }
                .onFailure { /* card already removed */ }
        }
    }

    private fun saveMatch(matchId: String) {
        viewModelScope.launch {
            radarService.saveMatch(matchId)
                .onSuccess { analytics.track(AppAnalytics.Events.RADAR_MATCH_SAVED) }
                .onFailure { e -> _events.send(RadarEvent.OnError(e.toUiText())) }
        }
    }
}
