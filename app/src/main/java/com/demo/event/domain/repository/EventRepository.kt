package com.demo.event.domain.repository

import com.demo.event.domain.model.Event
import kotlinx.coroutines.flow.Flow


interface EventRepository {

    fun observeEvents(): Flow<List<Event>>

    suspend fun getEvent(
        eventId: String
    ): Event?

    suspend fun refreshEvents(): Result<Unit>

    suspend fun toggleBookmark(
        eventId: String,
        bookmarked: Boolean
    )
}