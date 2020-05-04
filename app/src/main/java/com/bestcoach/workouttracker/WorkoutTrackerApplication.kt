package com.bestcoach.workouttracker

import android.app.Application
import timber.log.Timber

class WorkoutTrackerApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}