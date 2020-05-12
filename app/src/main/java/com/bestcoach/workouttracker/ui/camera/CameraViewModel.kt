package com.bestcoach.workouttracker.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val _startButtonString = MutableLiveData<String>("Start")
    val startButtonString: LiveData<String>
        get() = _startButtonString

    fun onStartClicked() {
        if (_startButtonString.value == "Start") {
            _startButtonString.value = "Stop"
        } else {
            _startButtonString.value = "Start"
        }
    }
}

