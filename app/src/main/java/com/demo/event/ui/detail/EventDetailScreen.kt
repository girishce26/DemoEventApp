package com.demo.event.ui.detail


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.demo.event.location.MapsNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        event?.let { item ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(item.location)

                Spacer(modifier = Modifier.height(8.dp))

                Text(item.time)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.toggleBookmark() }
                ) {
                    Text(
                        if (item.isBookmarked) "Remove Bookmark"
                        else "Bookmark"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        MapsNavigator.openDirections(
                            context,
                            item.latitude,
                            item.longitude,
                            item.title
                        )
                    }
                ) {
                    Text("Get Directions")
                }
            }
        }
    }
}