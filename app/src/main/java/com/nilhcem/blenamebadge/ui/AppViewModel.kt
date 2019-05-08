package com.nilhcem.blenamebadge.ui

import androidx.lifecycle.ViewModel
import com.nilhcem.blenamebadge.data.fragments.FilesRepository

class AppViewModel(private val configRepo: FilesRepository)
    : ViewModel() {

    fun getFiles() = configRepo.getFiles()

    fun deleteFile(fileName: String) = configRepo.deleteFile(fileName)

    fun updateList() = configRepo.update()

    fun getAbsPath(fileName: String): String? = configRepo.getAbsPath(fileName)
}