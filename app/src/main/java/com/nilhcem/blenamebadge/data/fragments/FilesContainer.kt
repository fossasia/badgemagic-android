package com.nilhcem.blenamebadge.data.fragments

class FilesContainer private constructor() {

    var filesData = FilesDAO()
        private set

    companion object {
        @Volatile
        private var instance: FilesContainer? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: FilesContainer().also { instance = it }
            }
    }
}