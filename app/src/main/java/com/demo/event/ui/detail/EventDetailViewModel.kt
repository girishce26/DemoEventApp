package com.demo.event.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.event.domain.model.Event
import com.demo.event.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _event =
        MutableStateFlow<Event?>(null)

    val event: StateFlow<Event?> =
        _event.asStateFlow()

    fun loadEvent(id: String) {

        viewModelScope.launch {

            _event.value =
                repository.getEvent(id)
        }
    }

    fun toggleBookmark() {

        viewModelScope.launch {

            _event.value?.let { event ->

                val newValue =
                    !event.isBookmarked

                repository.toggleBookmark(
                    event.id,
                    newValue
                )

                _event.value =
                    event.copy(
                        isBookmarked = newValue
                    )
            }
        }
    }
}