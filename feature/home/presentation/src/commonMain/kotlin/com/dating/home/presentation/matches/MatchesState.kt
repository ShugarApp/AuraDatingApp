package com.dating.home.presentation.matches

import com.dating.core.presentation.util.UiText
import com.dating.home.domain.matching.PendingSafetyCheck

enum class MatchesTab { MATCHES, LIKES }

enum class MatchesViewMode { GRID, LIST }

data class MatchesState(
    val selectedTab: MatchesTab = MatchesTab.MATCHES,
    val viewMode: MatchesViewMode = MatchesViewMode.GRID,
    val isLoading: Boolean = false,
    val isCreatingChat: Boolean = false,
    val matches: List<Match> = emptyList(),
    val likes: List<Match> = emptyList(),
    val error: UiText? = null,
    val showDeleteMatchDialog: Boolean = false,
    val matchToDelete: Match? = null,
    val isDeletingMatch: Boolean = false,
    // Módulo 4 — safety checks post-cita pendientes de responder (RN-4.8).
    val pendingSafetyChecks: List<PendingSafetyCheck> = emptyList()
)

data class Match(
    val id: String,
    val username: String,
    val profilePictureUrl: String?,
    val photos: List<String> = emptyList(),
    val city: String?,
    val country: String?,
    val age: Int? = null,
    val intention: String = "open",
    // Módulo 4 — 48h lifecycle (null for likes).
    val matchId: String? = null,
    val state: String = "ACTIVE",
    val expiresAt: String? = null,
    val revivedOnce: Boolean = false,
    // Módulo 3 — nota del like recibido (RN-3.4), solo en la pestaña de likes.
    val likeNote: String? = null
)

sealed interface MatchesAction {
    data object OnRefresh : MatchesAction
    data class OnTabSelected(val tab: MatchesTab) : MatchesAction
    data class OnMatchClick(val matchId: String, val imageUrl: String?) : MatchesAction
    data class OnStartChat(val matchId: String) : MatchesAction
    data class OnLikeClick(val userId: String, val imageUrl: String?) : MatchesAction
    data class OnLikeUser(val userId: String) : MatchesAction
    data class OnDislikeUser(val userId: String) : MatchesAction
    data class OnDeleteMatchClick(val match: Match) : MatchesAction
    // Módulo 4 — revive an expired match (RN-4.5).
    data class OnReviveMatch(val matchId: String) : MatchesAction
    // Módulo 4 — responder un safety check post-cita (RN-4.8). response = 'ok' | 'report'.
    data class OnAnswerSafetyCheck(val id: String, val response: String) : MatchesAction
    data object OnConfirmDeleteMatch : MatchesAction
    data object OnDismissDeleteMatchDialog : MatchesAction
    data object OnToggleViewMode : MatchesAction
}

sealed interface MatchesEvent {
    data class Error(val error: UiText) : MatchesEvent
    data class NavigateToChat(val chatId: String) : MatchesEvent
    data class NavigateToProfile(val userId: String, val imageUrl: String?) : MatchesEvent
    data object MatchDeleted : MatchesEvent
}
