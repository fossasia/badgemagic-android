package org.fossasia.badgemagic.ui

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.saved_files.FilesRepository

class AppViewModel(private val configRepo: FilesRepository) :
    ViewModel() {

    fun getFiles() = configRepo.getFiles()

    fun deleteFile(fileName: String) = configRepo.deleteFile(fileName)

    fun updateList() = configRepo.update()

    fun getAbsPath(fileName: String): String? = configRepo.getAbsPath(fileName)
}