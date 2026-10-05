package com.demo.event.ui.events

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.demo.event.location.DistanceCalculator
import com.demo.event.location.LocationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    onEventClick: (String) -> Unit,
    viewModel: EventListViewModel =
        hiltViewModel()
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle()

    val context = LocalContext.current

    var userLatitude by remember {
        mutableStateOf<Double?>(null)
    }

    var userLongitude by remember {
        mutableStateOf<Double?>(null)
    }

    val locationManager =
        remember {
            LocationManager(context)
        }

    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permissions[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (granted) {

                coroutineScope.launch {
                    val location =
                        locationManager
                            .getCurrentLocation()

                    userLatitude =
                        location?.latitude

                    userLongitude =
                        location?.longitude
                }
            }
        }

    LaunchedEffect(Unit) {

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {

            val location =
                locationManager
                    .getCurrentLocation()

            userLatitude =
                location?.latitude

            userLongitude =
                location?.longitude

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    var showNoEvents by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.events, state.isLoading) {
        if (!state.isLoading && state.events.isEmpty()) {
            showNoEvents = false
            delay(1000.milliseconds)
            if (state.events.isEmpty()) {
                showNoEvents = true
            }
        } else {
            showNoEvents = false
        }
    }
    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Nearby Events")
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when {
                state.isLoading && state.events.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.events.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.events,
                            key = { it.id }
                        ) { event ->

                            val distance =
                                if (
                                    userLatitude != null &&
                                    userLongitude != null
                                ) {
                                    DistanceCalculator
                                        .calculateDistance(
                                            userLatitude!!,
                                            userLongitude!!,
                                            event.latitude,
                                            event.longitude
                                        )
                                        .let(
                                            DistanceCalculator::formatDistance
                                        )
                                } else {
                                    null
                                }

                            EventItem(
                                event = event,
                                distance = distance,
                                onClick = {
                                    onEventClick(event.id)
                                },
                                onBookmarkClick = {
                                    viewModel.toggleBookmark(event)
                                }
                            )
                        }
                    }
                }
                showNoEvents -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No event found")
                    }
                }
            }

            state.error?.let { error ->

                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }



        }
    }
}

