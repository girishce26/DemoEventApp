package com.demo.event.ui.events

import com.demo.event.domain.model.Event
import com.demo.event.domain.repository.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModelTest {

    private val repository: EventRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { repository.observeEvents() } returns flowOf(emptyList())
        coEvery { repository.refreshEvents() } returns Result.success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun clearError_resetsErrorState() = runTest {
        val viewModel = EventListViewModel(repository)
        viewModel.clearError()
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun refresh_failure_setsErrorState() = runTest {
        val errorMessage = "Network Error"
        coEvery { repository.refreshEvents() } returns Result.failure(RuntimeException(errorMessage))

        val viewModel = EventListViewModel(repository)
        viewModel.refresh()
        testScheduler.advanceUntilIdle()

        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun toggleBookmark_callsRepository() = runTest {
        val event = Event(
            id = "1",
            title = "Test",
            location = "Loc",
            latitude = 0.0,
            longitude = 0.0,
            time = "2026-10-15T09:00:00",
            imageUrl = "",
            isBookmarked = false
        )

        val viewModel = EventListViewModel(repository)
        viewModel.toggleBookmark(event)
        testScheduler.advanceUntilIdle()

        coVerify { repository.toggleBookmark("1", true) }
    }
}
