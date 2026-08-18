package com.dating.home.data.radar

import com.dating.core.data.mappers.toDomain
import com.dating.core.data.networking.delete
import com.dating.core.data.networking.get
import com.dating.core.data.networking.post
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result
import com.dating.core.domain.util.map
import com.dating.home.domain.matching.SwipeAction
import com.dating.home.domain.radar.RadarProfile
import com.dating.home.domain.radar.RadarService
import com.dating.home.domain.radar.RadarSession
import com.dating.home.domain.radar.RadarZone
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorRadarService(private val httpClient: HttpClient) : RadarService {

    override suspend fun nearbyZones(lat: Double, lng: Double): Result<List<RadarZone>, DataError.Remote> {
        return httpClient.get<List<ZoneResponse>>(
            route = "/zones/nearby",
            queryParams = mapOf("lat" to lat, "lng" to lng)
        ).map { list -> list.map { RadarZone(it.id, it.name, it.kind) } }
    }

    override suspend fun startSession(zoneId: String, durationMinutes: Long): Result<RadarSession, DataError.Remote> {
        return httpClient.post<RadarSessionRequest, RadarSessionResponse>(
            route = "/radar/sessions",
            body = RadarSessionRequest(zoneId = zoneId, duration = durationMinutes)
        ).map { RadarSession(it.id, it.zoneId, it.zoneName, it.expiresAt.orEmpty()) }
    }

    override suspend fun endSession(reason: String): EmptyResult<DataError.Remote> {
        // DELETE with a body via the shared helper's builder.
        return httpClient.delete<Unit>(
            route = "/radar/sessions/current"
        ) {
            setBody(RadarEndRequest(reason))
        }
    }

    override suspend fun feed(): Result<List<RadarProfile>, DataError.Remote> {
        return httpClient.get<List<RadarProfileResponse>>(
            route = "/radar/feed"
        ).map { list -> list.map { RadarProfile(user = it.user.toDomain(), sharedZone = it.sharedZone) } }
    }

    override suspend fun swipe(targetId: String, action: SwipeAction): Result<Boolean, DataError.Remote> {
        return httpClient.post<RadarSwipeRequest, RadarSwipeResponse>(
            route = "/radar/swipe",
            body = RadarSwipeRequest(targetId = targetId, direction = action.name)
        ).map { it.isMatch }
    }

    override suspend fun saveMatch(matchId: String): Result<Boolean, DataError.Remote> {
        return httpClient.post<Unit, RadarSaveResponse>(
            route = "/radar/matches/$matchId/save",
            body = Unit
        ).map { it.converted }
    }
}
