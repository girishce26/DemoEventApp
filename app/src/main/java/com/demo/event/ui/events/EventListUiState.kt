package com.demo.event.ui.events

import com.demo.event.domain.model.Event

data class EventListUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)