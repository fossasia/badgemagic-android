package com.nilhcem.blenamebadge.data.badge_preview

import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed

class PreviewRepository private constructor(private val data: PreviewDAO) {

    fun getPreviewDetails() = data.getPreviewDetails()

    fun update(hexStrings: List<String>, flash: Boolean = false, marquee: Boolean = false, speed: Speed = Speed.ONE, mode: Mode = Mode.LEFT) = data.update(hexStrings, flash, marquee, speed, mode)

    fun textToLEDHex(s: String, checked: Boolean): Pair<Boolean, List<String>> = data.textToLEDHex(s, checked)

    fun fixLEDHex(allHex: List<String>, isInverted: Boolean): List<String> = data.fixLEDHex(allHex, isInverted)

    companion object {
        @Volatile
        private var instance: PreviewRepository? = null

        fun getInstance(data: PreviewDAO) =
            instance ?: synchronized(this) {
                instance
                    ?: PreviewRepository(data).also { instance = it }
            }
    }
}