package com.udacity.asteroidradar.works

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.local.AppDatabase.Companion.getDatabase
import com.udacity.asteroidradar.data.repo.AsteroidRepo
import com.udacity.asteroidradar.utils.WORK_SLEEP_DURATION
import com.udacity.asteroidradar.utils.makeStatusNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * @author Komi Donon
 * @since 5/15/2023
 */
class AsteroidRefreshWork(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidRefreshWork"
    }

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun doWork(): Result {
        val database = getDatabase(appContext)
        val repo = AsteroidRepo(database)

        return try {
            withContext(dispatcher) {
                repo.requestForNext7DaysAsteroids()
                makeStatusNotification("Download of the next 7 days asteroids.", appContext)
            }
            delay(WORK_SLEEP_DURATION)
            withContext(dispatcher) {
                repo.deletePreviousDayAsteroids()
                makeStatusNotification("Previous day's Asteroids deleted.", appContext)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
