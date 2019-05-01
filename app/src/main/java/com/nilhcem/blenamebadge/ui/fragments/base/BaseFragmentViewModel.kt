package com.nilhcem.blenamebadge.ui.fragments.base

import androidx.lifecycle.ViewModel
import com.nilhcem.blenamebadge.data.badge_preview.PreviewRepository
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.data.fragments.FilesRepository

class BaseFragmentViewModel(private val configRepo: FilesRepository, private val previewRepo: PreviewRepository)
    : ViewModel() {

    fun updatePreview(hexStrings: List<String>, flash: Boolean = false, marquee: Boolean = false, speed: Speed = Speed.ONE, mode: Mode = Mode.LEFT) = previewRepo.update(hexStrings, flash, marquee, speed, mode)

    fun getFiles() = configRepo.getFiles()

    fun deleteFile(fileName: String) = configRepo.deleteFile(fileName)

    fun updateList() = configRepo.update()

    fun saveFile(filename: String, json: String) = configRepo.saveFile(filename, json)

    fun textToLEDHex(text: String, inverted: Boolean): Pair<Boolean, List<String>> = previewRepo.textToLEDHex(text, inverted)

    fun fixLEDHex(allHex: List<String>, isInverted: Boolean): List<String> = previewRepo.fixLEDHex(allHex, isInverted)

    fun checkIfFilePresent(fileName: String): Boolean = configRepo.checkIfFilePresent(fileName)

    fun getAbsPath(fileName: String): String? = configRepo.getAbsPath(fileName)
}