package com.dating.home.presentation.radar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dating.core.presentation.permissions.Permission
import com.dating.core.presentation.permissions.PermissionState
import com.dating.core.presentation.permissions.rememberPermissionController
import com.dating.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RadarRoot(
    modifier: Modifier = Modifier,
    viewModel: RadarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val permissionController = rememberPermissionController()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RadarEvent.OnMatch -> snackbarState.showSnackbar("¡Es un match! Guárdalo antes de que termine la sesión.")
            is RadarEvent.OnError -> snackbarState.showSnackbar(event.message.asStringAsync())
        }
    }

    // Near-real-time reciprocity without WS: refresh the zone feed while broadcasting (RN-5.6).
    LaunchedEffect(state.isBroadcasting) {
        if (state.isBroadcasting) {
            var ticks = 0
            while (true) {
                delay(15_000)
                viewModel.onAction(RadarAction.OnRefreshFeed)
                // cada ~30s re-verifica que sigo dentro de mi zona (left_zone)
                if (++ticks % 2 == 0) viewModel.onAction(RadarAction.OnVerifyZone)
            }
        }
    }

    val onFindZones: () -> Unit = {
        scope.launch {
            val result = permissionController.requestPermission(Permission.LOCATION)
            if (result == PermissionState.GRANTED) {
                viewModel.onAction(RadarAction.OnFindZonesRequested)
            } else {
                snackbarState.showSnackbar("Necesitamos tu ubicación para el radar.")
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        RadarScreen(
            state = state,
            onAction = viewModel::onAction,
            onFindZones = onFindZones,
            modifier = Modifier.fillMaxSize()
        )
        SnackbarHost(
            hostState = snackbarState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
