package org.fossasia.badgemagic.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.fossasia.badgemagic.data.ConfigInfo
import org.fossasia.badgemagic.util.StorageUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class StorageFilesService : KoinComponent {
    private val files = MutableLiveData<List<ConfigInfo>>()
    private val storageUtils: StorageUtils by inject()

    init {
        files.value = storageUtils.getAllFiles()
    }

    fun deleteFile(fileName: String) {
        storageUtils.deleteFile(fileName)
        files.value = storageUtils.getAllFiles()
    }

    fun getFiles(): LiveData<List<ConfigInfo>> = files

    fun update() {
        files.value = storageUtils.getAllFiles()
    }

    fun saveFile(filename: String, json: String) {
        storageUtils.saveFile(filename, json)
    }

    fun getAbsPath(fileName: String): String = storageUtils.getAbsolutePathofFiles(fileName)

    fun checkIfFilePresent(fileName: String): Boolean = storageUtils.checkIfFilePresent(fileName)
}
