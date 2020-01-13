package org.fossasia.badgemagic.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import org.fossasia.badgemagic.data.badge.Badges

const val PREFS_FILENAME = "org.fossasia.badgemagic.prefs"
const val SELECTED_LANGUAGE = "selected_language"
const val SELECTED_BADGE = "selected_badge"

class PreferenceUtils(val context: Context) {

    private fun getPrefs() = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var selectedLanguage: Int
        get() = getPrefs()?.getInt(SELECTED_LANGUAGE, 0) ?: 0
        set(value) {
            getPrefs()?.edit()?.putInt(SELECTED_LANGUAGE, value)?.apply()
        }

    var selectedBadge: String
        get() = getPrefs()?.getString(SELECTED_BADGE, Badges.values()[0].toString())
            ?: Badges.values()[0].toString()
        set(value) {
            getPrefs()?.edit()?.putString(SELECTED_BADGE, value)?.apply()
        }
}
