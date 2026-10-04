package com.demo.event.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.event.domain.model.Event

@Entity(tableName = "events")
data class EventEntity(

    @PrimaryKey
    val id: String,

    val title: String,

    val location: String,

    val latitude: Double,

    val longitude: Double,

    val time: String,

    val imageUrl: String,

    val isBookmarked: Boolean,

    val fetchedAt: Long
)

fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        title = title,
        location = location,
        latitude = latitude,
        longitude = longitude,
        time = time,
        imageUrl = imageUrl,
        isBookmarked = isBookmarked
    )
}