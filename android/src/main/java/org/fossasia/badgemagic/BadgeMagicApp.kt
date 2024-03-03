package org.fossasia.badgemagic

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import org.fossasia.badgemagic.di.singletonModules
import org.fossasia.badgemagic.di.utilModules
import org.fossasia.badgemagic.di.viewModelModules
import org.fossasia.badgemagic.util.LocaleManager
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
                listOf(
                    singletonModules,
                    utilModules,
                    viewModelModules
                )
            )
        }

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }
}
