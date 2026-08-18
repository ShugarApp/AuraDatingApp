package com.dating.home.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReputationResponse(
    val status: String,
    @SerialName("strikes_count")
    val strikesCount: Int,
    @SerialName("reputation_score")
    val reputationScore: Int,
    @SerialName("public_flag_until")
    val publicFlagUntil: String? = null
)
