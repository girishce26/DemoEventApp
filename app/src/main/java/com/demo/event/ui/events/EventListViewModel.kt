package com.demo.event.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.event.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            EventListUiState()
        )

    val uiState: StateFlow<EventListUiState> =
        _uiState.asStateFlow()

    init {
        observeEvents()
    }

    private fun observeEvents() {

        viewModelScope.launch {

            repository.observeEvents()
                .collect { events ->

                    _uiState.update {
                        it.copy(
                            events = events
                        )
                    }
                }
        }
    }

}