package com.demo.event.data.repository

import com.demo.event.data.local.EventDao
import com.demo.event.data.local.EventEntity
import com.demo.event.data.local.toDomain
import com.demo.event.data.remote.EventDto
import com.demo.event.data.remote.MockEventDataSource
import com.demo.event.domain.model.Event
import com.demo.event.domain.repository.EventRepository
import com.demo.event.utility.AppConstants.CACHE_TTL_MILLIS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.text.compareTo

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

    override suspend fun getEvent(
        eventId: String
    ): Event? {

        return dao.getEvent(eventId)
            ?.toDomain()
    }

    override suspend fun refreshEvents(): Result<Unit> {

        return try {

            val now =
                System.currentTimeMillis()

            val lastFetched =
                dao.getLastFetchedTime()

            if (
                lastFetched != null &&
                now - lastFetched < CACHE_TTL_MILLIS
            ) {
                return Result.success(Unit)
            }

            val events = dataSource.getEvents() // Get from mock data
//                api.getEvents() // Get from Network call

            val existingEvents =
                dao.observeEvents().first()

            val bookmarks =
                existingEvents.associate {
                    it.id to it.isBookmarked
                }

            val apiEventIds = events.map { it.id }.toSet()

            val deletedEventIds = existingEvents
                .map { it.id }
                .filter { it !in apiEventIds }

            deletedEventIds.forEach { eventId ->
                dao.deleteEvent(eventId)
            }

            val entities =
                events.map { dto ->

                    dto.toEntity(
                        fetchedAt = now,
                        bookmarked =
                            bookmarks[dto.id] ?: false
                    )
                }

            dao.insertEvents(entities)

            Result.success(Unit)

        } catch (exception: Exception) {

            Result.failure(exception)
        }
    }

    override suspend fun toggleBookmark(
        eventId: String,
        bookmarked: Boolean
    ) {
        dao.updateBookmark(
            eventId = eventId,
            bookmarked = bookmarked
        )
    }
}
private fun EventDto.toEntity(
    fetchedAt: Long,
    bookmarked: Boolean
): EventEntity {

    return EventEntity(
        id = id,
        title = title,
        location = location,
        latitude = latitude,
        longitude = longitude,
        time = time,
        imageUrl = imageUrl,
        isBookmarked = bookmarked,
        fetchedAt = fetchedAt
    )
}
