package org.fossasia.badgemagic

import android.app.Application
import android.content.Context
import org.fossasia.badgemagic.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BadgeMagicApp : Application() {

    companion object {
        @JvmStatic
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                appModules
            )
        }

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
