package com.nilhcem.blenamebadge.device

import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.junit.Test

class DataToByteArrayConverterTest {

    @Test
    fun `should throw when we try to send more than 8 messages`() {
        // Given
        val data = DataToSend(List(9, { Message("") }))

        // When
        val function = { DataToByteArrayConverter.convert(data) }

        // Then
        function `should throw` IllegalStateException::class
    }

    @Test
    fun `result should start with 77616E67`() {
        // Given
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data).first()

        // Then
        result[0] `should be equal to` 0x77
        result[1] `should be equal to` 0x61
        result[2] `should be equal to` 0x6E
        result[3] `should be equal to` 0x67
    }
}
