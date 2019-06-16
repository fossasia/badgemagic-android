package org.fossasia.badgemagic.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

const val PREFS_FILENAME = "org.fossasia.badgemagic.prefs"
const val SELECTED_LANGUAGE = "selected_language"

class PreferenceUtils(val context: Context) {

    private fun getPrefs() = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var selectedLanguage: Int
        get() = getPrefs()?.getInt(SELECTED_LANGUAGE, 0) ?: 0
        set(value) {
            getPrefs()?.edit()?.putInt(SELECTED_LANGUAGE, value)?.apply()
        }
}