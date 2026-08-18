package com.dating.home.data.radar

import com.dating.core.data.dto.UserSerializable
import kotlinx.serialization.Serializable

@Serializable
data class ZoneResponse(val id: String, val name: String, val kind: String)

@Serializable
data class RadarSessionRequest(val zoneId: String, val duration: Long)

@Serializable
data class RadarEndRequest(val reason: String)

@Serializable
data class RadarSessionResponse(
    val id: String,
    val zoneId: String,
    val zoneName: String,
    val expiresAt: String? = null
)

@Serializable
data class RadarProfileResponse(val user: UserSerializable, val sharedZone: String)

@Serializable
data class RadarSwipeRequest(val targetId: String, val direction: String)

@Serializable
data class RadarSwipeResponse(val isMatch: Boolean)

@Serializable
data class RadarSaveResponse(val converted: Boolean)
