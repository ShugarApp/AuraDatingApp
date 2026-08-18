package com.dating.home.domain.matching

import com.dating.core.domain.auth.User
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result

enum class SwipeAction { LIKE, DISLIKE }

data class SwipeResult(val isMatch: Boolean)

// Módulo 3 — the scarce daily deck and received likes with their notes.
data class Deck(
    val profiles: List<User>,
    val remaining: Int,
    val resetsAt: String
)

data class ReceivedLike(
    val user: User,
    val likeNote: String?
)

// Módulo 4 — a match plus its 48h lifecycle metadata.
data class MatchInfo(
    val matchId: String,
    val user: User,
    val state: String,
    val expiresAt: String,
    val revivedOnce: Boolean,
    val origin: String
)

// Módulo 4 — venue curado para el primer encuentro (RN-4.9).
data class Venue(
    val id: String,
    val name: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val isPublicSpace: Boolean
)

interface MatchingService {
    suspend fun getFeed(
        gender: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        maxDistance: Double? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<User>, DataError.Remote>

    // Módulo 3 — daily deck (RN-3.1) and note-gated like (RN-3.3).
    suspend fun getDeck(
        gender: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        maxDistance: Double? = null
    ): Result<Deck, DataError.Remote>

    suspend fun discoverySwipe(
        targetId: String,
        action: SwipeAction,
        likeNote: String? = null
    ): Result<SwipeResult, DataError.Remote>

    suspend fun getLikesWithNotes(): Result<List<ReceivedLike>, DataError.Remote>

    suspend fun swipe(
        swipedId: String,
        action: SwipeAction
    ): Result<SwipeResult, DataError.Remote>

    suspend fun getMatches(): Result<List<MatchInfo>, DataError.Remote>

    // Módulo 4 — revive an expired match once (RN-4.5). Caller reloads matches after.
    suspend fun reviveMatch(matchId: String): EmptyResult<DataError.Remote>
    // Módulo 4 — venues curados (RN-4.9), safety check (RN-4.8) y transiciones de plan (RN-4.2).
    suspend fun getVenues(category: String? = null): Result<List<Venue>, DataError.Remote>
    suspend fun answerSafetyCheck(safetyCheckId: String, response: String): EmptyResult<DataError.Remote>
    suspend fun markPlanProposed(matchId: String): EmptyResult<DataError.Remote>
    suspend fun markPlanConfirmed(matchId: String): EmptyResult<DataError.Remote>

    suspend fun getLikes(): Result<List<User>, DataError.Remote>

    suspend fun deleteMatch(matchedUserId: String): EmptyResult<DataError.Remote>

    suspend fun undoSwipe(swipedId: String): EmptyResult<DataError.Remote>
}
