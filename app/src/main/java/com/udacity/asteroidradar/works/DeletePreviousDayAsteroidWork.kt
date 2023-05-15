package com.udacity.asteroidradar.works

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.local.AppDatabase.Companion.getDatabase
import com.udacity.asteroidradar.data.repo.AsteroidRepo
import com.udacity.asteroidradar.utils.WORK_SLEEP_DURATION
import com.udacity.asteroidradar.utils.makeStatusNotification
import kotlinx.coroutines.delay

/**
 * @author Komi Donon
 * @since 5/15/2023
 */
class DeletePreviousDayAsteroidWork(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val DELETE_WORK_NAME = "DeletePreviousDayAsteroidWork"
    }

    override suspend fun doWork(): Result {
        makeStatusNotification("Start deleting previous day's Asteroids.", appContext)
        delay(WORK_SLEEP_DURATION)
        val database = getDatabase(appContext)
        val repo = AsteroidRepo(database)

        return try {
            repo.deletePreviousDayAsteroids()
            makeStatusNotification("Previous day's Asteroids deleted.", appContext)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
