package com.nilhcem.blenamebadge.ui

import android.app.Application
import com.nilhcem.blenamebadge.BuildConfig
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
