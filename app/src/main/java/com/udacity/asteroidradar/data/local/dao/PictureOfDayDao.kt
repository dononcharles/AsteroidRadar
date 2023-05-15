package com.udacity.asteroidradar.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.models.PictureOfDay

/**
 * @author Komi Donon
 * @since 5/13/2023
 */

@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureOfDay: PictureOfDay)

    @Query("SELECT * FROM picture_of_day LIMIT 1")
    fun getPictureOfDay(): LiveData<PictureOfDay?>
}
