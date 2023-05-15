package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.udacity.asteroidradar.works.AsteroidRefreshWork
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * @author Komi Donon
 * @since 5/13/2023
 */
class ARApplication : Application() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        delayInit()
    }

    private fun delayInit() {
        applicationScope.launch(coroutineExceptionHandler) {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidRefreshWork>(repeatInterval = 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        /*   val repeatingRequest2 = PeriodicWorkRequestBuilder<DeletePreviousDayAsteroidWork>(repeatInterval = 1, TimeUnit.DAYS)
               .setConstraints(constraints)
               .build()*/

        /*   WorkManager.getInstance(applicationContext)
               .enqueue(mutableListOf(repeatingRequest, repeatingRequest2)*/

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                AsteroidRefreshWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest,
            )
    }
}
