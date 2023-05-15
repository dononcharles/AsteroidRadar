package com.udacity.asteroidradar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.models.Asteroid
import kotlinx.coroutines.flow.Flow

/**
 * @author Komi Donon
 * @since 5/12/2023
 */

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroids: Asteroid)

    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): List<Asteroid>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate = :today")
    fun getTodayAsteroids(today: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate BETWEEN :today AND :week")
    fun getWeekAsteroids(today: String, week: String): List<Asteroid>

    @Query("SELECT * FROM asteroid WHERE isPotentiallyHazardous = 1 ORDER BY closeApproachDate DESC")
    fun getSavedAsteroids(): List<Asteroid>

    @Query("DELETE FROM asteroid")
    suspend fun deleteAsteroids()

    @Query("DELETE FROM asteroid WHERE closeApproachDate = :currentDate")
    suspend fun deleteAsteroidsByDate(currentDate: String)

    @Query("DELETE FROM asteroid WHERE closeApproachDate BETWEEN :today AND :week")
    suspend fun deleteWeekAsteroids(today: String, week: String)

    @Query("DELETE FROM asteroid WHERE isPotentiallyHazardous = 1")
    suspend fun deleteSavedAsteroids()
}
