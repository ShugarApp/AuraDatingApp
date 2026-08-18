package com.dating.home.data.dto

import kotlinx.serialization.Serializable

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
