package com.dating.home.data.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequestDto(
    val reason: String,
    val description: String? = null,
    val messageId: String? = null,
    val matchId: String? = null,
    val matchIntention: String? = null,
    val category: String? = null
)

@Serializable
data class ReportResponseDto(
    val id: String,
    val message: String
)
