package com.nilhcem.blenamebadge.device

import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import org.amshove.kluent.`should equal`
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
    fun `result should start with 77616E67000000`() {
        // Given
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected: List<Byte> = listOf(0x77, 0x61, 0x6E, 0x67, 0x00, 0x00)
        result.take(6) `should equal` expected
    }

    private fun List<ByteArray>.join(): ByteArray {
        var byteArray = ByteArray(0)
        forEach { byteArray = byteArray.plus(it) }
        return byteArray
    }
}
