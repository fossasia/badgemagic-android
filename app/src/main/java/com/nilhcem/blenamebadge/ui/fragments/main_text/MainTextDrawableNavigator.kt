package com.nilhcem.blenamebadge.ui.fragments.main_text

import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.data.device.model.DataToSend

interface MainTextDrawableNavigator {
    fun selectText()

    fun selectDrawable(selectedItem: DrawableInfo?)

    fun convertTextToDeviceDataModel(): DataToSend

    fun convertBitmapToDeviceDataModel(): DataToSend

    fun setupRecyclerViews()

    fun removeListeners()

    fun getCurrentDate(): String

    fun showSaveFileDialog()

    fun configToJSON(): String

    fun showFileOverrideDialog(fileName: String, jsonString: String)

    fun saveFile(fileName: String, jsonString: String)
}
