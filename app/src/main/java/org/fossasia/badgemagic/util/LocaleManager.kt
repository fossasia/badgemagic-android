package org.fossasia.badgemagic.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale
import org.fossasia.badgemagic.data.Language

object LocaleManager {

    fun setLocale(context: Context?): Context {
        return updateResources(context as Context,
            Language.values()[PreferenceUtils(context).selectedLanguage].locale
        )
    }

    fun updateResources(context: Context, language: Locale): Context {

        val contextFun: Context

        Locale.setDefault(language)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(language)
        contextFun = context.createConfigurationContext(configuration)

        return contextFun
    }
}
