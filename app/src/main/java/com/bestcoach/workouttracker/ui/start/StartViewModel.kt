package com.bestcoach.workouttracker.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel(){
    private val _eventNavigateToCamera = MutableLiveData<Boolean>()
    val eventNavigateToCamera : LiveData<Boolean>
    get() = _eventNavigateToCamera

    fun doneNavigatingToCamera(){
        _eventNavigateToCamera.value = false
    }

    fun onStartClicked(){
        _eventNavigateToCamera.value = true
    }
}
