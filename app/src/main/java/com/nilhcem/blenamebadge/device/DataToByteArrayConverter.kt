package com.nilhcem.blenamebadge.device

import com.nilhcem.blenamebadge.device.model.DataToSend

object DataToByteArrayConverter {

    private const val MAX_MESSAGES = 8

    fun convert(data: DataToSend): ByteArray {
        check(data.messages.size <= MAX_MESSAGES) { "Max messages=$MAX_MESSAGES"}

        return ByteArray(0)
    }
}
