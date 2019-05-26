package org.fossasia.badgemagic.ui.fragments.main_textart

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.clipart.ClipArtRepository
import org.fossasia.badgemagic.data.saved_files.FilesRepository

class MainTextArtViewModel(private val configRepo: FilesRepository, private val clipArtRepo: ClipArtRepository) : ViewModel() {
    var speed = 1
    var isFlash = false
    var isMarquee = false
    var isInverted = false
    var animationPosition = -1
    var currentTab = 1
    var text = ""

    fun checkIfFilePresent(fileName: String): Boolean = configRepo.checkIfFilePresent(fileName)

    fun updateList() = configRepo.update()

    fun saveFile(filename: String, json: String) = configRepo.saveFile(filename, json)

    fun getClipArts() = clipArtRepo.getClipArts()
}