package com.dating.home.data.dto.response

import com.dating.core.data.dto.UserSerializable
import kotlinx.serialization.Serializable

@Serializable
data class DeckResponse(
    val profiles: List<UserSerializable> = emptyList(),
    val remaining: Int = 0,
    val resetsAt: String? = null
)

@Serializable
data class ReceivedLikeResponse(
    val user: UserSerializable,
    val likeNote: String? = null
)
