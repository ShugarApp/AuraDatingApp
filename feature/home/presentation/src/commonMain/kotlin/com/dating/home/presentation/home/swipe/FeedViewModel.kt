package com.dating.home.presentation.home.swipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dating.core.domain.analytics.AppAnalytics
import com.dating.core.domain.auth.SessionStorage
import com.dating.core.domain.auth.VerificationStatus
import com.dating.core.domain.auth.profileCompletion
import com.dating.core.domain.discovery.DiscoveryPreferencesStorage
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.dating.core.domain.discovery.Gender
import com.dating.core.domain.util.onFailure
import com.dating.core.domain.util.onSuccess
import com.dating.home.domain.matching.MatchingService
import com.dating.home.domain.matching.SwipeAction
import com.dating.home.domain.user.UserService
import kotlin.time.Clock.System.now
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val matchingService: MatchingService,
    private val discoveryPreferences: DiscoveryPreferencesStorage,
    private val sessionStorage: SessionStorage,
    private val userService: UserService,
    private val analytics: AppAnalytics
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = _state.asStateFlow()

    private val _events = Channel<FeedEvent>()
    val events = _events.receiveAsFlow()

    private var isInitialized = false
    private val blockedUserIds = mutableSetOf<String>()

    init {
        observeAccountPaused()
        observeIncognitoMode()
        viewModelScope.launch {
            val prefs = discoveryPreferences.get()
            val authInfo = sessionStorage.observeAuthInfo().first()
            _state.update {
                it.copy(
                    minAge = prefs.minAge,
                    maxAge = prefs.maxAge,
                    maxDistance = prefs.maxDistance,
                    showMe = prefs.showMe,
                    showVerifiedOnly = prefs.verifiedProfilesOnly,
                    currentUserPhotoUrl = authInfo?.user?.profilePictureUrl
                )
            }
            val isPaused = authInfo?.user?.isPaused == true
            if (!isPaused) {
                loadFeed(page = 0, isInitialLoad = true)
            }
            isInitialized = true
            checkCompleteProfilePrompt()
        }
    }

    private fun observeAccountPaused() {
        viewModelScope.launch {
            var wasPaused: Boolean? = null
            sessionStorage.observeAuthInfo().collect { authInfo ->
                val isPaused = authInfo?.user?.isPaused == true
                val justResumed = wasPaused == true && !isPaused
                _state.update {
                    it.copy(isAccountPaused = isPaused)
                }
                if (justResumed) {
                    resetAndLoadFeed()
                }
                wasPaused = isPaused
            }
        }
    }

    private fun observeIncognitoMode() {
        viewModelScope.launch {
            sessionStorage.observeAuthInfo().collect { authInfo ->
                val isIncognito = authInfo?.user?.isIncognito == true
                _state.update { it.copy(isIncognitoActive = isIncognito) }
            }
        }
    }

    private suspend fun checkCompleteProfilePrompt() {
        if (discoveryPreferences.isCompleteProfilePromptShown()) return
        val user = sessionStorage.observeAuthInfo().first()?.user ?: return
        if (user.profileCompletion() < 70) {
            val dismissCount = discoveryPreferences.getCompleteProfileDismissCount()
            if (dismissCount >= 2) {
                discoveryPreferences.setCompleteProfilePromptShown()
                _events.send(FeedEvent.NavigateToProfileSetup)
            } else {
                _state.update { it.copy(showCompleteProfileDialog = true) }
            }
        }
    }

    fun onAction(action: FeedAction) {
        when (action) {
            is FeedAction.OnRefresh -> resetAndLoadFeed()
            is FeedAction.OnSwipeRight -> onLikeIntent(action.userId)
            is FeedAction.OnSwipeLeft -> swipe(action.userId, SwipeAction.DISLIKE)
            is FeedAction.OnUserClick -> {
                viewModelScope.launch {
                    _events.send(FeedEvent.NavigateToProfile(action.userId, action.imageUrl))
                }
            }
            is FeedAction.OnFiltersApplied -> {
                _state.update {
                    it.copy(
                        maxDistance = action.distance,
                        showMe = action.gender,
                        minAge = action.minAge,
                        maxAge = action.maxAge,
                        showVerifiedOnly = action.showVerifiedOnly,
                        feedItems = emptyList(),
                        currentPage = 0,
                        hasMore = true
                    )
                }
                viewModelScope.launch {
                    discoveryPreferences.updateMaxDistance(action.distance)
                    discoveryPreferences.updateShowMe(action.gender)
                    discoveryPreferences.updateAgeRange(action.minAge, action.maxAge)
                    discoveryPreferences.updateVerifiedProfilesOnly(action.showVerifiedOnly)
                }
                loadFeed(page = 0, isInitialLoad = true)
            }
            FeedAction.OnDismissMatchDialog -> {
                _state.update { it.copy(showMatchDialog = false, matchedUserId = null, matchedUserName = null, matchedUserPhotoUrl = null) }
            }
            FeedAction.OnMatchSendMessage -> {
                _state.update { it.copy(showMatchDialog = false, matchedUserId = null, matchedUserName = null, matchedUserPhotoUrl = null) }
                viewModelScope.launch { _events.send(FeedEvent.NavigateToMatches) }
            }
            FeedAction.OnCompleteProfileClick -> {
                _state.update { it.copy(showCompleteProfileDialog = false) }
                viewModelScope.launch {
                    discoveryPreferences.setCompleteProfilePromptShown()
                    _events.send(FeedEvent.NavigateToProfileSetup)
                }
            }
            FeedAction.OnDismissCompleteProfileDialog -> {
                _state.update { it.copy(showCompleteProfileDialog = false) }
                viewModelScope.launch {
                    discoveryPreferences.incrementCompleteProfileDismissCount()
                }
            }
            FeedAction.OnScreenResumed -> {
                if (isInitialized) refreshPreferencesIfChanged()
            }
            is FeedAction.OnUserSwiped -> {
                val swipedItem = _state.value.feedItems.firstOrNull { it.userId == action.userId }
                _state.update { current ->
                    current.copy(
                        feedItems = current.feedItems.filter { it.userId != action.userId },
                        lastDislikedItem = if (action.isDislike) swipedItem else null
                    )
                }
                prefetchIfNeeded()
            }
            FeedAction.OnUndoSwipe -> undoSwipe()
            FeedAction.OnResumeAccount -> resumeAccount()
            is FeedAction.OnUserBlocked -> removeUser(action.userId)
            is FeedAction.OnSubmitLikeNote -> submitLikeNote(action.note)
            FeedAction.OnDismissLikeNote -> cancelLikeNote()
        }
    }

    // Módulo 3 — a like requires a note (RN-3.3). Open the note sheet; remove the card
    // optimistically (matching the swipe animation) and restore it if the user cancels.
    private fun onLikeIntent(userId: String) {
        val item = _state.value.feedItems.firstOrNull { it.userId == userId } ?: return
        _state.update {
            it.copy(
                feedItems = it.feedItems.filter { i -> i.userId != userId },
                pendingLikeItem = item,
                showLikeNoteSheet = true,
                likeNoteError = false
            )
        }
    }

    private fun cancelLikeNote() {
        _state.update { current ->
            val restored = current.pendingLikeItem?.let { listOf(it) + current.feedItems } ?: current.feedItems
            current.copy(
                feedItems = restored,
                pendingLikeItem = null,
                showLikeNoteSheet = false,
                likeNoteError = false,
                isDeckExhausted = restored.isEmpty()
            )
        }
    }

    private fun submitLikeNote(note: String) {
        if (note.trim().length < LIKE_NOTE_MIN_LENGTH) {
            _state.update { it.copy(likeNoteError = true) }
            return
        }
        val item = _state.value.pendingLikeItem ?: return
        _state.update {
            it.copy(showLikeNoteSheet = false, pendingLikeItem = null, likeNoteError = false)
        }
        analytics.track(AppAnalytics.Events.LIKE_WITH_NOTE)
        performSwipe(item, SwipeAction.LIKE, note.trim())
    }

    private fun removeUser(userId: String) {
        blockedUserIds.add(userId)
        resetAndLoadFeed()
    }

    private fun resumeAccount() {
        _state.update { it.copy(isResumingAccount = true) }
        viewModelScope.launch {
            userService.pauseAccount(false)
                .onSuccess { updatedUser ->
                    val authInfo = sessionStorage.observeAuthInfo().first()
                    if (authInfo != null) {
                        sessionStorage.set(authInfo.copy(user = updatedUser))
                    }
                    _state.update { it.copy(isResumingAccount = false) }
                }
                .onFailure {
                    _state.update { it.copy(isResumingAccount = false) }
                }
        }
    }

    private fun refreshPreferencesIfChanged() {
        viewModelScope.launch {
            val prefs = discoveryPreferences.get()
            val current = _state.value
            if (prefs.minAge != current.minAge ||
                prefs.maxAge != current.maxAge ||
                prefs.maxDistance != current.maxDistance ||
                prefs.showMe != current.showMe ||
                prefs.verifiedProfilesOnly != current.showVerifiedOnly
            ) {
                _state.update {
                    it.copy(
                        minAge = prefs.minAge,
                        maxAge = prefs.maxAge,
                        maxDistance = prefs.maxDistance,
                        showMe = prefs.showMe,
                        showVerifiedOnly = prefs.verifiedProfilesOnly,
                        feedItems = emptyList(),
                        currentPage = 0,
                        hasMore = true
                    )
                }
                loadFeed(page = 0, isInitialLoad = true)
            }
        }
    }

    private fun resetAndLoadFeed() {
        _state.update {
            it.copy(
                feedItems = emptyList(),
                currentPage = 0,
                hasMore = true,
                lastDislikedItem = null
            )
        }
        loadFeed(page = 0, isInitialLoad = true)
    }

    private fun loadFeed(page: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            if (isInitialLoad) {
                _state.update { it.copy(isLoading = true, hasConnectionError = false) }
            } else {
                _state.update { it.copy(isFetchingMore = true) }
            }
            val s = _state.value
            val genderFilter = when (s.showMe) {
                Gender.EVERYONE -> null
                else -> s.showMe.apiValue
            }
            matchingService.getDeck(
                gender = genderFilter,
                minAge = s.minAge,
                maxAge = s.maxAge,
                maxDistance = s.maxDistance
            )
                .onSuccess { deck ->
                    val newItems = deck.profiles
                        .filter { it.id !in blockedUserIds }
                        .map { user ->
                            FeedItem(
                                userId = user.id,
                                username = user.username,
                                profilePictureUrl = user.profilePictureUrl,
                                photoUrls = user.photos,
                                city = user.city,
                                country = user.country,
                                age = calculateAge(user.birthDate),
                                isVerified = user.verificationStatus == VerificationStatus.VERIFIED,
                                intention = user.intention,
                                publicFlagUntil = user.publicFlagUntil
                            )
                        }
                    if (newItems.isEmpty()) analytics.track(AppAnalytics.Events.DECK_EXHAUSTED)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isFetchingMore = false,
                            feedItems = newItems,
                            remaining = deck.remaining,
                            resetsAt = deck.resetsAt,
                            isDeckExhausted = newItems.isEmpty(),
                            hasMore = false
                        )
                    }
                }
                .onFailure { _ ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isFetchingMore = false,
                            hasConnectionError = isInitialLoad
                        )
                    }
                }
        }
    }

    // Módulo 3 — the deck is a fixed daily set; there is no pagination/prefetch.
    private fun prefetchIfNeeded() = Unit

    private fun swipe(userId: String, action: SwipeAction) {
        val swipedItem = _state.value.feedItems.firstOrNull { it.userId == userId } ?: return
        _state.update { current ->
            val remainingItems = current.feedItems.filter { it.userId != userId }
            current.copy(
                feedItems = remainingItems,
                lastDislikedItem = if (action == SwipeAction.DISLIKE) swipedItem else null,
                isDeckExhausted = remainingItems.isEmpty()
            )
        }
        performSwipe(swipedItem, action, likeNote = null)
    }

    // Módulo 3 — all swipes go through the deck swipe endpoint so the server records the
    // like note (RN-3.3) and decrements the deck. The card is already removed by the caller.
    private fun performSwipe(item: FeedItem, action: SwipeAction, likeNote: String?) {
        viewModelScope.launch {
            matchingService.discoverySwipe(targetId = item.userId, action = action, likeNote = likeNote)
                .onSuccess { result ->
                    _state.update {
                        val newRemaining = (it.remaining - 1).coerceAtLeast(0)
                        it.copy(
                            remaining = newRemaining,
                            isDeckExhausted = it.feedItems.isEmpty(),
                            showMatchDialog = result.isMatch || it.showMatchDialog,
                            matchedUserId = if (result.isMatch) item.userId else it.matchedUserId,
                            matchedUserName = if (result.isMatch) item.username else it.matchedUserName,
                            matchedUserPhotoUrl = if (result.isMatch) item.profilePictureUrl else it.matchedUserPhotoUrl
                        )
                    }
                }
                .onFailure { /* swipe errors are non-critical; card already removed */ }
        }
    }

    private fun undoSwipe() {
        val lastDisliked = _state.value.lastDislikedItem ?: return
        _state.update { it.copy(isUndoing = true) }
        viewModelScope.launch {
            matchingService.undoSwipe(swipedId = lastDisliked.userId)
                .onSuccess {
                    _state.update { current ->
                        current.copy(
                            feedItems = listOf(lastDisliked) + current.feedItems,
                            lastDislikedItem = null,
                            isUndoing = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isUndoing = false) }
                }
        }
    }

    companion object {
        const val LIKE_NOTE_MIN_LENGTH = 40
    }
}

private fun calculateAge(birthDate: String?): Int? {
    if (birthDate == null) return null
    return try {
        val birth = LocalDate.parse(birthDate.substring(0, 10))
        val today = now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        var age = today.year - birth.year
        if (today.monthNumber < birth.monthNumber ||
            (today.monthNumber == birth.monthNumber && today.dayOfMonth < birth.dayOfMonth)) {
            age--
        }
        age
    } catch (e: Exception) {
        null
    }
}
