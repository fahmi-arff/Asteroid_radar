package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    // setup for get today & day of seven
    private val getDays = getNextSevenDaysFormattedDates()
    private val startDate = getDays.first()
    private val endDate = getDays.last()

    // convert database data as domain for showing to screen
    val weekAsteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsByDate(startDate, endDate)) {
            it.asDomainModel()
        }

    val todayAsteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroid(startDate)) {
            it.asDomainModel()
        }

    val savedAsteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    // calling the api & save asteroid data with dispatcher IO to database
    suspend fun refreshAsteroidData() {
        var asteroidListData: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val response = AsteroidApi.retrofitService.getAsteroidsAsync(startDate, endDate)
            asteroidListData = parseAsteroidsJsonResult(JSONObject(response.string()))
            database.asteroidDao.insertAll(*asteroidListData.asDatabaseModel())
        }
    }

    // delete yesterday data from database
    suspend fun deleteYesterdayAsteroid() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteYesterdayAsteroids(startDate)
        }
    }
}


