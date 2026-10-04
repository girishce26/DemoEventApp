package com.demo.event.domain.repository

import com.demo.event.domain.model.Event
import kotlinx.coroutines.flow.Flow


interface EventRepository {

    fun observeEvents(): Flow<List<Event>>

}