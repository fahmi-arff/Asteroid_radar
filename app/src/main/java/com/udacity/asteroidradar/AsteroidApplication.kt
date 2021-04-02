package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.workManager.DeleteYesterdayDataWorker
import com.udacity.asteroidradar.workManager.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // delayUnit so app start first & work do in the another thread (not block app to launch)
    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        // constraint for efficency app
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        // app repeating request once a day
        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()

        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<DeleteYesterdayDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()

        // workmanager call the instance do the job
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshRequest
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DeleteYesterdayDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingDeleteRequest
        )
    }

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
}