package org.fossasia.badgemagic.data.clipart

class ClipArtContainer private constructor() {

    var clipArts = ClipArtDAO()
        private set

    companion object {
        @Volatile
        private var instance: ClipArtContainer? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: ClipArtContainer().also { instance = it }
            }
    }
}