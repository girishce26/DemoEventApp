package com.demo.event.ui.detail


import com.demo.event.domain.model.Event
import com.demo.event.domain.repository.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventDetailViewModelTest {

    private val repository: EventRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadEvent_updatesEventState() = runTest {
        val testEvent = Event(
            id = "1",
            title = "Test Event",
            location = "Location",
            latitude = 0.0,
            longitude = 0.0,
            time = "2026-10-15T09:00:00",
            imageUrl = "",
            isBookmarked = false
        )
        coEvery { repository.getEvent("1") } returns testEvent

        val viewModel = EventDetailViewModel(repository)
        viewModel.loadEvent("1")
        testScheduler.advanceUntilIdle()

        assertEquals(testEvent, viewModel.event.value)
    }

    @Test
    fun toggleBookmark_updatesBookmarkState() = runTest {
        val testEvent = Event(
            id = "1",
            title = "Test Event",
            location = "Location",
            latitude = 0.0,
            longitude = 0.0,
            time = "2026-10-15T09:00:00",
            imageUrl = "",
            isBookmarked = false
        )
        coEvery { repository.getEvent("1") } returns testEvent

        val viewModel = EventDetailViewModel(repository)
        viewModel.loadEvent("1")
        testScheduler.advanceUntilIdle()

        viewModel.toggleBookmark()
        testScheduler.advanceUntilIdle()

        assertEquals(true, viewModel.event.value?.isBookmarked)
        coVerify { repository.toggleBookmark("1", true) }
    }
}
