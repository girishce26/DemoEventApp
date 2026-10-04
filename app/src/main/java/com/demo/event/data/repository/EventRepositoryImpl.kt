package com.demo.event.data.repository

import com.demo.event.data.local.EventDao
import com.demo.event.data.local.toDomain
import com.demo.event.data.remote.MockEventDataSource
import com.demo.event.domain.model.Event
import com.demo.event.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
//    private val api: EventApi,// Network call data
    private val dataSource: MockEventDataSource,// Mock data
    private val dao: EventDao
) : EventRepository {

    override fun observeEvents(): Flow<List<Event>> {

        return dao.observeEvents()
            .map { entities ->
                entities.map {
                    it.toDomain()
                }
            }
    }
}
