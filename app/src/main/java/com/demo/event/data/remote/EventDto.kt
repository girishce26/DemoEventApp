package com.demo.event.data.remote

import com.demo.event.domain.model.Event

data class EventDto(
    val id: String,
    val title: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val time: String,
    val imageUrl: String
)

fun EventDto.toDomain(): Event {
    return Event(
        id = id,
        title = title,
        location = location,
        latitude = latitude,
        longitude = longitude,
        time = time,
        imageUrl = imageUrl
    )
}