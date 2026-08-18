package com.dating.home.presentation.profile.reputation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dating.core.domain.auth.Reputation
import com.dating.core.domain.util.onFailure
import com.dating.core.domain.util.onSuccess
import com.dating.core.presentation.util.UiText
import com.dating.core.presentation.util.toUiText
import com.dating.home.domain.user.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReputationState(
    val isLoading: Boolean = true,
    val reputation: Reputation? = null,
    val error: UiText? = null
)

class ReputationViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _state = MutableStateFlow(ReputationState())
    val state = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            userService.getReputation()
                .onSuccess { rep -> _state.update { it.copy(isLoading = false, reputation = rep) } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.toUiText()) } }
        }
    }
}
