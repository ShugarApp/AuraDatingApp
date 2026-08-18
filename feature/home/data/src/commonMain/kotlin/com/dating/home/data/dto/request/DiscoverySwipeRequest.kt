package com.dating.home.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverySwipeRequest(
    val targetId: String,
    val direction: String,
    val likeNote: String? = null
)
