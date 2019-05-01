package com.nilhcem.blenamebadge.ui.fragments.main_saved

import com.nilhcem.blenamebadge.data.fragments.ConfigInfo

interface MainSavedNavigator {
    fun setupRecycler()

    fun showLoadAlert(item: ConfigInfo)

    fun setPreview(badgeJSON: String)

    fun setPreviewNull()
}
