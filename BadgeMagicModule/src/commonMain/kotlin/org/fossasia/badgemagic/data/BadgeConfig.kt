package org.fossasia.badgemagic.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val CONF_HEX_STRINGS = "hex_strings"
const val CONF_SPEED = "speed"
const val CONF_MODE = "mode"
const val CONF_FLASH = "flash"
const val CONF_MARQUEE = "marquee"
const val CONF_INVERTED = "inverted"

@Serializable
class BadgeConfig {
    @SerialName(CONF_HEX_STRINGS)
    var hexStrings: List<String> = mutableListOf()

    @SerialName(CONF_SPEED)
    var speed: Speed = Speed.ONE

    @SerialName(CONF_MODE)
    var mode: Mode = Mode.LEFT

    @SerialName(CONF_FLASH)
    var isFlash: Boolean = false

    @SerialName(CONF_MARQUEE)
    var isMarquee: Boolean = false

    @SerialName(CONF_INVERTED)
    var isInverted: Boolean = false
}
