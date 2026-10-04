package com.demo.event.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {


    @Query(
        """
        SELECT * FROM events
        ORDER BY time ASC
        """
    )
    fun observeEvents(): Flow<List<EventEntity>>

    @Query(
        """
        SELECT * FROM events
        WHERE id = :eventId
        LIMIT 1
        """
    )
    suspend fun getEvent(
        eventId: String
    ): EventEntity?


    @Query(
        """
        SELECT MAX(fetchedAt)
        FROM events
        """
    )
    suspend fun getLastFetchedTime(): Long?

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertEvents(
        events: List<EventEntity>
    )

    @Query(
        """
        UPDATE events
        SET isBookmarked = :bookmarked
        WHERE id = :eventId
        """
    )
    suspend fun updateBookmark(
        eventId: String,
        bookmarked: Boolean
    )

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: String)

}