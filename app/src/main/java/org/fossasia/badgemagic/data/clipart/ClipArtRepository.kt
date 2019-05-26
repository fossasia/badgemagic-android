package org.fossasia.badgemagic.data.clipart

class ClipArtRepository private constructor(private val clipArts: ClipArtDAO) {

    fun getClipArts() = clipArts.getClipArts()

    companion object {
        @Volatile
        private var instance: ClipArtRepository? = null

        fun getInstance(filesData: ClipArtDAO) =
            instance ?: synchronized(this) {
                instance
                    ?: ClipArtRepository(filesData).also { instance = it }
            }
    }
}