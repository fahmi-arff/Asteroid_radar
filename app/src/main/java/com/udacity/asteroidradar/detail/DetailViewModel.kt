package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    private val _displayUnit = MutableLiveData<Boolean>()
    val displayUnit: LiveData<Boolean>
        get() = _displayUnit

    fun onUnitIconClicked(){
        _displayUnit.value = true
    }

    fun onUnitIconClickedComplete(){
        _displayUnit.value = false
    }
}