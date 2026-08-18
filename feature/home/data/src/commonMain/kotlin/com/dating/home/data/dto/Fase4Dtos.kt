package com.dating.home.data.dto

import com.dating.core.data.dto.UserSerializable
import kotlinx.serialization.Serializable

@Serializable
data class PendingSafetyCheckResponse(
    val id: String,
    val matchId: String,
    val user: UserSerializable,
    val askedAt: String
)

@Serializable
data class VenueResponse(
    val id: String,
    val name: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val isPublicSpace: Boolean = true
)

@Serializable
data class SafetyAnswerRequest(val response: String)   // 'ok' | 'report'
