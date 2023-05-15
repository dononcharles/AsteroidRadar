package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.domains.AsteroidApiStatus
import com.udacity.asteroidradar.data.local.AppDatabase.Companion.getDatabase
import com.udacity.asteroidradar.data.local.models.Asteroid
import com.udacity.asteroidradar.data.remote.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.repo.AsteroidRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidRepo(database)

    private val _loadingAsteroidState = MutableStateFlow(AsteroidApiStatus.IDLE)
    val loadingAsteroidState = _loadingAsteroidState.asStateFlow()

    private val _asteroidList = MutableStateFlow(emptyList<Asteroid>())
    val asteroidList = _asteroidList.asStateFlow()

    /**
     * Picture of the day from the offline cache.
     */
    val pictureOfTheDay = asteroidRepo.pictureOfDay

    /**
     * Refresh the asteroids stored in the offline cache.
     */
    init {
        viewModelScope.launch {
            launch { getAsteroidList() }
            launch { asteroidRepo.requestForNext7DaysAsteroids() }
            launch { asteroidRepo.getPicturesOfTheDay() }
        }
    }

    /**
     * Fetch today's asteroids from the offline cache.
     */
    private fun getAsteroidList() {
        viewModelScope.launch {
            _loadingAsteroidState.value = AsteroidApiStatus.LOADING
            delay(1000)
            try {
                val dates = getNextSevenDaysFormattedDates()
                asteroidRepo.getTodayAsteroids(dates.first()).distinctUntilChanged().collectLatest {
                    _asteroidList.value = it
                    _loadingAsteroidState.value = AsteroidApiStatus.DONE
                }
            } catch (e: Exception) {
                _loadingAsteroidState.value = AsteroidApiStatus.ERROR
            }
        }
    }
}
