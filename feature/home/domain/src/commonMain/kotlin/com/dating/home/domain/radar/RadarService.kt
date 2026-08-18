package com.dating.home.domain.radar

import com.dating.core.domain.auth.User
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result
import com.dating.home.domain.matching.SwipeAction

/** A radar zone the user can broadcast in (Módulo 5). Never exposes coordinates. */
data class RadarZone(val id: String, val name: String, val kind: String)

/** An active radar session. */
data class RadarSession(val id: String, val zoneId: String, val zoneName: String, val expiresAt: String)

/** Someone visible in the radar feed — only the shared zone is shown, never distance/position. */
data class RadarProfile(val user: User, val sharedZone: String)

interface RadarService {
    suspend fun nearbyZones(lat: Double, lng: Double): Result<List<RadarZone>, DataError.Remote>
    suspend fun startSession(zoneId: String, durationMinutes: Long): Result<RadarSession, DataError.Remote>
    suspend fun endSession(reason: String): EmptyResult<DataError.Remote>
    suspend fun feed(): Result<List<RadarProfile>, DataError.Remote>
    // Like in radar needs no note (RN-5.3).
    suspend fun swipe(targetId: String, action: SwipeAction): Result<Boolean, DataError.Remote>
    suspend fun saveMatch(matchId: String): Result<Boolean, DataError.Remote>
}
