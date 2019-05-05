package com.nilhcem.blenamebadge.ui

import android.app.Application
import android.content.Context
import com.nilhcem.blenamebadge.BuildConfig
import timber.log.Timber

class App : Application() {

    companion object {
        @JvmStatic
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
