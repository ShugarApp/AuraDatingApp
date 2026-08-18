package com.dating.core.domain.auth

/** The current user's trust & safety standing (Módulo 2, RN-2.7). Never shown for other users. */
data class Reputation(
    val status: String,
    val strikesCount: Int,
    val reputationScore: Int,
    val publicFlagUntil: String?
)
