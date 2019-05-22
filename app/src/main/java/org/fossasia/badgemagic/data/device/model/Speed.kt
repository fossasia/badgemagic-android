package org.fossasia.badgemagic.data.device.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class Speed(val hexValue: Byte) {
    ONE(0x00),
    TWO(0x10),
    THREE(0x20),
    FOUR(0x30),
    FIVE(0x40),
    SIX(0x50),
    SEVEN(0x60),
    EIGHT(0x70);

    class Adapter {
        @ToJson
        fun toJson(status: Speed): Int {
            return status.ordinal
        }

        @FromJson
        fun fromJson(value: Int): Speed {
            return values()[value]
        }
    }
}
