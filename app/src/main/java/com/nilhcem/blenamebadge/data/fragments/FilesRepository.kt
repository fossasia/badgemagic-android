package com.nilhcem.blenamebadge.data.fragments

class FilesRepository private constructor(private val filesData: FilesDAO) {

    fun deleteFile(fileName: String) = filesData.deleteFile(fileName)

    fun getFiles() = filesData.getFiles()

    fun update() = filesData.update()

    fun saveFile(filename: String, json: String) = filesData.saveFile(filename, json)

    fun checkIfFilePresent(fileName: String): Boolean = filesData.checkIfFilePresent(fileName)

    fun getAbsPath(fileName: String): String? = filesData.getAbsPath(fileName)

    companion object {
        @Volatile
        private var instance: FilesRepository? = null

        fun getInstance(filesData: FilesDAO) =
            instance ?: synchronized(this) {
                instance
                    ?: FilesRepository(filesData).also { instance = it }
            }
    }
}