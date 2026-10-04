package com.demo.event.di

import android.content.Context
import androidx.room.Room
import com.demo.event.data.local.EventDao
import com.demo.event.data.local.EventsDatabase
import com.demo.event.data.remote.EventApi
import com.demo.event.data.remote.MockEventDataSource
import com.demo.event.data.repository.EventRepositoryImpl
import com.demo.event.domain.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://demo.com/"
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EventsDatabase {

        return Room.databaseBuilder(
            context,
            EventsDatabase::class.java,
            "nearby_events.db"
        ).build()
    }

    @Provides
    fun provideEventDao(
        database: EventsDatabase
    ): EventDao {

        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideEventApi(
        retrofit: Retrofit
    ): EventApi {

        return retrofit.create(
            EventApi::class.java
        )
    }

    // This is for Network call
    /*@Provides
    @Singleton
    fun provideRepository(
        api: EventApi,
        dao: EventDao
    ): EventRepository {

        return EventRepositoryImpl(
            api = api,
            dao = dao
        )
    }*/

    // This is for Mock data source
    @Provides
    @Singleton
    fun provideRepository(
        dataSource: MockEventDataSource,
        dao: EventDao
    ): EventRepository {

        return EventRepositoryImpl(
            dataSource = dataSource,
            dao = dao
        )
    }
}