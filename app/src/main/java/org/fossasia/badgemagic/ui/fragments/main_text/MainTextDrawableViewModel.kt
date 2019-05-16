package org.fossasia.badgemagic.ui.fragments.main_text

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.fragments.FilesRepository

class MainTextDrawableViewModel(private val configRepo: FilesRepository) : ViewModel() {
    var speed = 1
    var isFlash = false
    var isMarquee = false
    var isInverted = false
    var drawablePosition = -1
    var animationPosition = -1
    var radioSelectedId = -1
    var currentTab = 1
    var text = ""

    fun checkIfFilePresent(fileName: String): Boolean = configRepo.checkIfFilePresent(fileName)

    fun updateList() = configRepo.update()

    fun saveFile(filename: String, json: String) = configRepo.saveFile(filename, json)
}