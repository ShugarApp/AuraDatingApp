package com.dating.home.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IntentionStatusResponse(
    val intention: String,
    @SerialName("can_change")
    val canChange: Boolean,
    @SerialName("next_available_at")
    val nextAvailableAt: String? = null
)
