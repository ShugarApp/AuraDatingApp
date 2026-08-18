package com.dating.home.data.dto.response

import com.dating.core.data.dto.UserSerializable
import kotlinx.serialization.Serializable

@Serializable
data class MatchResponse(
    val matchId: String,
    val user: UserSerializable,
    val state: String = "ACTIVE",
    val expiresAt: String? = null,
    val revivedOnce: Boolean = false,
    val origin: String = "DISCOVERY"
)

// Response of the match-state transition endpoints (revive/plan-*). No user payload.
@Serializable
data class MatchStateResponse(
    val id: String,
    val state: String = "ACTIVE",
    val expiresAt: String? = null,
    val revivedOnce: Boolean = false
)
