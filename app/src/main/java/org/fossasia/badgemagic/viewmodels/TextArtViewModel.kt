package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.database.ClipArtService
import org.fossasia.badgemagic.database.StorageFilesService

class TextArtViewModel(
    private val clipArtService: ClipArtService,
    private val storageFilesService: StorageFilesService
) : ViewModel() {
    var speed = 1
    var isFlash = false
    var isMarquee = false
    var isInverted = false
    var animationPosition = -1
    var currentTab = 1
    var text = ""

    fun checkIfFilePresent(fileName: String): Boolean = storageFilesService.checkIfFilePresent(fileName)

    fun updateList() = storageFilesService.update()

    fun saveFile(filename: String, json: String) = storageFilesService.saveFile(filename, json)

    fun getClipArts() = clipArtService.getClipArts()
}