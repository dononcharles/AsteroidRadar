package com.udacity.asteroidradar.data.repo

import com.udacity.asteroidradar.data.local.AppDatabase
import com.udacity.asteroidradar.data.remote.Network
import com.udacity.asteroidradar.data.remote.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.remote.parseAsteroidsJsonResult
import com.udacity.asteroidradar.utils.API_QUERY_DATE_FORMAT
import org.json.JSONObject
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * @author Komi Donon
 * @since 5/12/2023
 */
class AsteroidRepo(private val appDatabase: AppDatabase) {

    private val dateFormatter = DateTimeFormatter.ofPattern(API_QUERY_DATE_FORMAT)

    /**
     * Refresh the asteroids stored in the offline cache.
     */
    fun getTodayAsteroids(todayDate: String) = appDatabase.asteroidDao.getTodayAsteroids(todayDate)

    fun getWeekAsteroids(startDate: String, endDate: String) = appDatabase.asteroidDao.getWeekAsteroids(startDate, endDate)

    fun getSavedAsteroids() = appDatabase.asteroidDao.getSavedAsteroids()

    suspend fun deletePreviousDayAsteroids() {
        appDatabase.asteroidDao.deleteAsteroidsByDate(OffsetDateTime.now().minusDays(1).format(dateFormatter))
    }

    /**
     * Network fetch and insert into database
     */

    suspend fun requestForNext7DaysAsteroids() {
        Network.asteroidApi.getAsteroidsByDate(
            startDate = getNextSevenDaysFormattedDates().first(),
            endDate = getNextSevenDaysFormattedDates().last(),
        ).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    val asteroidList = parseAsteroidsJsonResult(JSONObject(result))
                    appDatabase.asteroidDao.insertAll(asteroidList)
                }
            }
        }
    }

    suspend fun requestTodayAsteroids() {
        Network.asteroidApi.getAsteroidsByDate(
            startDate = getNextSevenDaysFormattedDates().first(),
            endDate = getNextSevenDaysFormattedDates().first(),
        ).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    val asteroidList = parseAsteroidsJsonResult(JSONObject(result))
                    appDatabase.asteroidDao.insertAll(asteroidList)
                }
            }
        }
    }

    suspend fun getPicturesOfTheDay() {
        val pictureOfDay = Network.asteroidApi.getPictureOfDay()
        if (pictureOfDay.isSuccessful) {
            pictureOfDay.body()?.let { result ->
                if (result.isImage) appDatabase.pictureOfDayDao.insert(result)
            }
        }
    }

    /**
     * Picture of the day from the offline cache.
     */
    val pictureOfDay = appDatabase.pictureOfDayDao.getPictureOfDay()
}
