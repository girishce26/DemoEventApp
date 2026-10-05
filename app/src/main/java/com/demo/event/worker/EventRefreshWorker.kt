package com.demo.event.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.demo.event.domain.repository.EventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EventRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: EventRepository
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {

        return repository
            .refreshEvents()
            .fold(

                onSuccess = {
                    Log.d("EventRefreshWorker","Worker success")
                    Result.success()
                },

                onFailure = {
                    Log.d("EventRefreshWorker","Worker failed")
                    Result.retry()
                }
            )
    }
}