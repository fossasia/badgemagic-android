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
        result.slice(0..5) `should equal` listOf<Byte>(0x77, 0x61, 0x6E, 0x67, 0x00, 0x00)
    }

    @Test
    fun `marquee should be 00 when no messages have marquee option enabled`() {
        // Given
        val data = DataToSend(listOf(Message("A", false)))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        result.slice(7..7) `should equal` listOf(0x00.toByte())
    }

    @Test
    fun `marquee should contain 8 bits, each bit representing the marquee value of each message, 1 when marquee is enabled, 0 otherwise`() {
        // Given
        val data = DataToSend(listOf(
                Message("A", marquee = true),
                Message("A", marquee = false),
                Message("A", marquee = true),
                Message("A", marquee = false),
                Message("A", marquee = false),
                Message("A", marquee = false),
                Message("A", marquee = true),
                Message("A", marquee = true)))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected = 0xC5.toByte() // Binary: 11000101
        result.slice(7..7) `should equal` listOf(expected)
    }

    private fun List<ByteArray>.join(): ByteArray {
        var byteArray = ByteArray(0)
        forEach { byteArray = byteArray.plus(it) }
        return byteArray
    }
}
