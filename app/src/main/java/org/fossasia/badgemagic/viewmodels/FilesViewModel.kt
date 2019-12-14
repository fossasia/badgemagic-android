package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.database.StorageFilesService

class FilesViewModel(
    private val storageFilesService: StorageFilesService
) : ViewModel() {

    fun getFiles() = storageFilesService.getFiles()

    fun deleteFile(fileName: String) = storageFilesService.deleteFile(fileName)

    fun getAbsPath(fileName: String): String? = storageFilesService.getAbsPath(fileName)
}
