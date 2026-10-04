package com.demo.event.data.remote

import retrofit2.http.GET


interface EventApi {

    @GET("events")
    suspend fun getEvents(): List<EventDto>
}