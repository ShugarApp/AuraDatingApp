package com.dating.home.data.matching

import com.dating.core.data.dto.UserSerializable
import com.dating.core.data.mappers.toDomain
import com.dating.core.data.networking.delete
import com.dating.core.data.networking.get
import com.dating.core.data.networking.post
import com.dating.core.domain.auth.User
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result
import com.dating.core.domain.util.map
import com.dating.home.data.dto.request.DiscoverySwipeRequest
import com.dating.home.data.dto.request.SwipeRequest
import com.dating.home.data.dto.response.DeckResponse
import com.dating.home.data.dto.response.MatchResponse
import com.dating.home.data.dto.response.MatchStateResponse
import com.dating.home.data.dto.response.ReceivedLikeResponse
import com.dating.home.data.dto.response.SwipeResponse
import com.dating.home.domain.matching.Deck
import com.dating.home.domain.matching.MatchInfo
import com.dating.home.domain.matching.MatchingService
import com.dating.home.domain.matching.ReceivedLike
import com.dating.home.domain.matching.Venue
import com.dating.home.domain.matching.SwipeAction
import com.dating.home.domain.matching.SwipeResult
import io.ktor.client.HttpClient

class KtorMatchingService(private val httpClient: HttpClient) : MatchingService {

    override suspend fun getDeck(
        gender: String?,
        minAge: Int?,
        maxAge: Int?,
        maxDistance: Double?
    ): Result<Deck, DataError.Remote> {
        val params = buildMap<String, Any> {
            if (gender != null) put("gender", gender)
            if (minAge != null) put("minAge", minAge)
            if (maxAge != null) put("maxAge", maxAge)
            if (maxDistance != null) put("maxDistance", maxDistance)
        }
        return httpClient.get<DeckResponse>(
            route = "/discovery/deck",
            queryParams = params
        ).map { dto ->
            Deck(
                profiles = dto.profiles.map { it.toDomain() },
                remaining = dto.remaining,
                resetsAt = dto.resetsAt.orEmpty()
            )
        }
    }

    override suspend fun discoverySwipe(
        targetId: String,
        action: SwipeAction,
        likeNote: String?
    ): Result<SwipeResult, DataError.Remote> {
        return httpClient.post<DiscoverySwipeRequest, SwipeResponse>(
            route = "/discovery/swipe",
            body = DiscoverySwipeRequest(
                targetId = targetId,
                direction = action.name,
                likeNote = likeNote?.takeIf { it.isNotBlank() }
            )
        ).map { SwipeResult(isMatch = it.isMatch) }
    }

    override suspend fun getLikesWithNotes(): Result<List<ReceivedLike>, DataError.Remote> {
        return httpClient.get<List<ReceivedLikeResponse>>(
            route = "/discovery/likes"
        ).map { list ->
            list.map { ReceivedLike(user = it.user.toDomain(), likeNote = it.likeNote) }
        }
    }

    override suspend fun getFeed(
        gender: String?,
        minAge: Int?,
        maxAge: Int?,
        maxDistance: Double?,
        page: Int,
        size: Int
    ): Result<List<User>, DataError.Remote> {
        val params = buildMap<String, Any> {
            if (gender != null) put("gender", gender)
            if (minAge != null) put("minAge", minAge)
            if (maxAge != null) put("maxAge", maxAge)
            if (maxDistance != null) put("maxDistance", maxDistance)
            put("page", page)
            put("size", size)
        }
        return httpClient.get<List<UserSerializable>>(
            route = "/matching/feed",
            queryParams = params
        ).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun swipe(
        swipedId: String,
        action: SwipeAction
    ): Result<SwipeResult, DataError.Remote> {
        return httpClient.post<SwipeRequest, SwipeResponse>(
            route = "/matching/swipe",
            body = SwipeRequest(swipedId = swipedId, action = action.name)
        ).map { SwipeResult(isMatch = it.isMatch) }
    }

    override suspend fun getMatches(): Result<List<MatchInfo>, DataError.Remote> {
        return httpClient.get<List<MatchResponse>>(
            route = "/matching/matches"
        ).map { list -> list.map { it.toMatchInfo() } }
    }

    override suspend fun reviveMatch(matchId: String): EmptyResult<DataError.Remote> {
        return httpClient.post<Unit, MatchStateResponse>(
            route = "/matches/$matchId/revive",
            body = Unit
        ).map { }
    }

    override suspend fun getVenues(category: String?): Result<List<Venue>, DataError.Remote> {
        val params = buildMap<String, Any> { if (category != null) put("category", category) }
        return httpClient.get<List<com.dating.home.data.dto.VenueResponse>>(
            route = "/venues",
            queryParams = params
        ).map { list -> list.map { Venue(it.id, it.name, it.category, it.lat, it.lng, it.isPublicSpace) } }
    }

    override suspend fun answerSafetyCheck(safetyCheckId: String, response: String): EmptyResult<DataError.Remote> {
        return httpClient.post<com.dating.home.data.dto.SafetyAnswerRequest, Unit>(
            route = "/safety-checks/$safetyCheckId/answer",
            body = com.dating.home.data.dto.SafetyAnswerRequest(response)
        )
    }

    override suspend fun markPlanProposed(matchId: String): EmptyResult<DataError.Remote> {
        return httpClient.post<Unit, MatchStateResponse>(
            route = "/matches/$matchId/plan-proposed", body = Unit
        ).map { }
    }

    override suspend fun markPlanConfirmed(matchId: String): EmptyResult<DataError.Remote> {
        return httpClient.post<Unit, MatchStateResponse>(
            route = "/matches/$matchId/plan-confirmed", body = Unit
        ).map { }
    }

    private fun MatchResponse.toMatchInfo() = MatchInfo(
        matchId = matchId,
        user = user.toDomain(),
        state = state,
        expiresAt = expiresAt.orEmpty(),
        revivedOnce = revivedOnce,
        origin = origin
    )

    override suspend fun getLikes(): Result<List<User>, DataError.Remote> {
        return httpClient.get<List<UserSerializable>>(
            route = "/matching/likes"
        ).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun deleteMatch(matchedUserId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/matching/matches/$matchedUserId"
        )
    }

    override suspend fun undoSwipe(swipedId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/matching/swipe/$swipedId"
        )
    }
}
