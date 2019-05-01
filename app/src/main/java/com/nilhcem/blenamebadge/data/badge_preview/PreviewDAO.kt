package com.nilhcem.blenamebadge.data.badge_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nilhcem.blenamebadge.data.device.model.Message
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.util.Converters

class PreviewDAO {
    private val currentPreview = MutableLiveData<Message>()

    init {
        currentPreview.value = Message(listOf())
    }

    fun update(hexStrings: List<String>, flash: Boolean = false, marquee: Boolean = false, speed: Speed = Speed.ONE, mode: Mode = Mode.LEFT) {
        currentPreview.value = Message(
            hexStrings,
            flash,
            marquee,
            speed,
            mode
        )
    }

    fun getPreviewDetails(): LiveData<Message> = currentPreview

    fun textToLEDHex(text: String, invertLED: Boolean): Pair<Boolean, List<String>> = Converters.convertTextToLEDHex(text, invertLED)

    fun fixLEDHex(allHex: List<String>, isInverted: Boolean): List<String> = Converters.fixLEDHex(allHex, isInverted)
}