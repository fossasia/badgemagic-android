package org.fossasia.badgemagic.data.saved_files

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