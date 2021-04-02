package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // setup database & repository
    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList : LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty

   private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getSeventhDayAsteroid()
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroidData()
            } catch (e: java.lang.Exception) {
                Log.i("MainViewModel_astro", e.message.toString())
            }
        }
        getImageOfTheDay()
    }

    // get image data with retrofit to PictureOfDay class
    private fun getImageOfTheDay() {
        var imgToday : PictureOfDay
        viewModelScope.launch {
            try {
                val imgToday = AsteroidApi.retrofitService.getImageOfTheDayAsync()
                _pictureOfDay.value = imgToday
            } catch (e: Exception) {
                _pictureOfDay.value = null
            }
        }
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    // calling the repository week asteroid with asFlow & collect as value
    // if access liveData as liveData like asteroid.value = repo.weekList.value
    // always timeout i don't know
    fun getSeventhDayAsteroid(){
        viewModelScope.launch {
            asteroidRepository.weekAsteroidList.asFlow().collect {
                _asteroidList.value = it
            }
        }
    }

    fun getTodayAsteroid(){
        viewModelScope.launch {
            asteroidRepository.todayAsteroidList.asFlow().collect {
                _asteroidList.value = it
            }
        }

    }

    fun getSavedAsteroid(){
        viewModelScope.launch {
            asteroidRepository.savedAsteroidList.asFlow().collect {
                _asteroidList.value = it
            }
        }

    }
}
