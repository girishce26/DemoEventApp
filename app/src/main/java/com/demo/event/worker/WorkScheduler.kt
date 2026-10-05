package com.demo.event.worker

import android.content.Context
import androidx.work.*
import com.demo.event.utility.AppConstants.REFRESH_INTERVAL_HOURS
import com.demo.event.utility.AppConstants.WORKER_UNIQUE_TAG
import java.util.concurrent.TimeUnit

class WorkScheduler {
    fun scheduleEventRefresh(
        context: Context
    ) {

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED
                )
                .build()

        val request =
            PeriodicWorkRequestBuilder<
                    EventRefreshWorker
                    >(
                REFRESH_INTERVAL_HOURS,
                TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                WORKER_UNIQUE_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}