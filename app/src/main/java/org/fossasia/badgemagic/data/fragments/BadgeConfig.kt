package org.fossasia.badgemagic.data.fragments

import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val CONF_HEX_STRINGS = "hex_strings"
const val CONF_SPEED = "speed"
const val CONF_MODE = "mode"
const val CONF_FLASH = "flash"
const val CONF_MARQUEE = "marquee"
const val CONF_INVERTED = "inverted"

@JsonClass(generateAdapter = true)
class BadgeConfig {
    @Json(name = CONF_HEX_STRINGS)
    var hexStrings: List<String> = mutableListOf()

    @Json(name = CONF_SPEED)
    var speed: Speed = Speed.ONE

    @Json(name = CONF_MODE)
    var mode: Mode = Mode.LEFT

    @Json(name = CONF_FLASH)
    var isFlash: Boolean = false

    @Json(name = CONF_MARQUEE)
    var isMarquee: Boolean = false

    @Json(name = CONF_INVERTED)
    var isInverted: Boolean = false
}