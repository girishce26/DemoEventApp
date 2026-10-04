package com.demo.event.data.remote

import android.content.Context
import com.demo.event.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MockEventDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    suspend fun getEvents(): List<EventDto> =
        withContext(Dispatchers.IO) {

            val inputStream =
                context.resources
                    .openRawResource(
                        R.raw.events
                    )

            val json =
                inputStream
                    .bufferedReader()
                    .use { it.readText() }

            val type =
                object :
                    TypeToken<List<EventDto>>() {}.type

            Gson().fromJson(
                json,
                type
            )
        }
}