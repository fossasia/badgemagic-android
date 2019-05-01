package com.nilhcem.blenamebadge.data.badge_preview

class PreviewContainer private constructor() {

    var previewData = PreviewDAO()
        private set

    companion object {
        @Volatile
        private var instance: PreviewContainer? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: PreviewContainer().also { instance = it }
            }
    }
}