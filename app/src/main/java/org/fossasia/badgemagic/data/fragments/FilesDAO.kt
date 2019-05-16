package org.fossasia.badgemagic.data.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.fossasia.badgemagic.util.StorageUtils

class FilesDAO {
    private val files = MutableLiveData<List<ConfigInfo>>()

    init {
        files.value = StorageUtils.getAllFiles()
    }

    fun deleteFile(fileName: String) {
        StorageUtils.deleteFile(fileName)
        files.value = StorageUtils.getAllFiles()
    }

    fun getFiles(): LiveData<List<ConfigInfo>> = files

    fun update() {
        files.value = StorageUtils.getAllFiles()
    }

    fun saveFile(filename: String, json: String) {
        StorageUtils.saveFile(filename, json)
    }

    fun getAbsPath(fileName: String): String? = StorageUtils.getAbsolutePathofFiles(fileName)

    fun checkIfFilePresent(fileName: String): Boolean = StorageUtils.checkIfFilePresent(fileName)
}