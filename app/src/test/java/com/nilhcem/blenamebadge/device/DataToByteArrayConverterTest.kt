package com.nilhcem.blenamebadge.device

import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
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
}
