package com.nilhcem.blenamebadge.device

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nilhcem.blenamebadge.core.utils.ByteArrayUtils
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should throw`
import org.junit.Test
import java.util.Calendar

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
    fun `result should start with 77616E670000`() {
        // Given
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        result.slice(0..5) `should equal` listOf<Byte>(0x77, 0x61, 0x6E, 0x67, 0x00, 0x00)
    }

    @Test
    fun `flash should be 0x00 when no messages have flash option enabled`() {
        // Given
        val data = DataToSend(listOf(Message("A", flash = false)))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        result.slice(6..6) `should equal` listOf(0x00.toByte())
    }

    @Test
    fun `flash should contain 8 bits, each bit representing the flash value of each message, 1 when flash is enabled, 0 otherwise`() {
        // Given
        val data = DataToSend(listOf(
                Message("A", flash = true),
                Message("A", flash = true),
                Message("A", flash = false),
                Message("A", flash = false),
                Message("A", flash = true),
                Message("A", flash = false),
                Message("A", flash = true),
                Message("A", flash = false)))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected = 0x53.toByte() // Binary: 01010011
        result.slice(6..6) `should equal` listOf(expected)
    }

    @Test
    fun `marquee should be 0x00 when no messages have marquee option enabled`() {
        // Given
        val data = DataToSend(listOf(Message("A", marquee = false)))

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

    @Test
    fun `option should be a single byte containing the speed and the mode, repeated for all 8 messages`() {
        // Given
        val data = DataToSend(listOf(
                Message("A", speed = Speed.ONE, mode = Mode.RIGHT),
                Message("A", speed = Speed.TWO, mode = Mode.LEFT),
                Message("A", speed = Speed.THREE, mode = Mode.UP),
                Message("A", speed = Speed.FOUR, mode = Mode.FIXED),
                Message("A", speed = Speed.SIX, mode = Mode.LASER),
                Message("A", speed = Speed.SEVEN, mode = Mode.SNOWFLAKE),
                Message("A", speed = Speed.EIGHT, mode = Mode.PICTURE)))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected = listOf<Byte>(0x01, 0x10, 0x22, 0x34, 0x58, 0x65, 0x76, 0x00)
        result.slice(8..15) `should equal` expected
    }

    @Test
    fun `size should contain the 2 bytes hexadecimal value for each message, skipping invalid characters if any`() {
        // Given
        val data = DataToSend(listOf(
                Message("A"),
                Message("..."),
                Message("abcdefghijklmnopqrstuvwxyz"),
                Message("-".repeat(500)),
                Message("É"),
                Message("ÇÇÇÇÇabc"),
                Message("")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected = listOf(0x00, 0x01, 0x00, 0x03, 0x00, 0x1A, 0x01, 0xF4.toByte(), 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00)
        result.slice(16..31) `should equal` expected
    }

    @Test
    fun `the 6 next bytes after the size should all be equal to 0x00`() {
        // Given
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        result.slice(32..37) `should equal` List(6) { 0x00.toByte() }
    }

    @Test
    fun `timestamp should contain 6 bytes, 1 for the last 2 digits of the year, 1 for the month, the day, the hour, the minute and the second`() {
        // Given
        val calendar = mock<Calendar> {
            on { get(Calendar.YEAR) } doReturn 2017
            on { get(Calendar.MONTH) } doReturn 10
            on { get(Calendar.DAY_OF_MONTH) } doReturn 3
            on { get(Calendar.HOUR_OF_DAY) } doReturn 23
            on { get(Calendar.MINUTE) } doReturn 50
            on { get(Calendar.SECOND) } doReturn 2
        }
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data, calendar).join()

        // Then
        result.slice(38..43) `should equal` listOf(0xE1.toByte(), 0x0B, 0x03, 0x17, 0x32, 0x02)
    }

    @Test
    fun `the 20 next bytes after the timestamp should all be equal to 0x00`() {
        // Given
        val data = DataToSend(listOf(Message("A")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        result.slice(44..63) `should equal` List(20) { 0x00.toByte() }
    }

    @Test
    fun `message should be located at the end and containing hex code for each character, skipping invalid characters`() {
        // Given
        val data = DataToSend(listOf(Message("AB"), Message("ÈC")))

        // When
        val result = DataToByteArrayConverter.convert(data).join()

        // Then
        val expected = "00386CC6C6FEC6C6C6C600" + "00FC6666667C666666FC00" + "007CC6C6C0C0C0C6C67C00" // A + B + C
        ByteArrayUtils.byteArrayToHexString(result.slice(64..96).toByteArray()) `should be equal to` expected
    }

    @Test
    fun `each packet should contain 16 bytes`() {
        // Given
        val data1 = DataToSend(listOf(Message("A")))
        val data2 = DataToSend(listOf(Message("B"), Message("BBB")))
        val data3 = DataToSend(listOf(Message("C"), Message("CCCCCCC"), Message("CCCCCCCCC")))

        // When
        val result1 = DataToByteArrayConverter.convert(data1)
        val result2 = DataToByteArrayConverter.convert(data2)
        val result3 = DataToByteArrayConverter.convert(data3)

        // Then
        result1.forEach { it.size `should equal` 16 }
        result2.forEach { it.size `should equal` 16 }
        result3.forEach { it.size `should equal` 16 }
    }

    private fun List<ByteArray>.join(): ByteArray {
        var byteArray = ByteArray(0)
        forEach { byteArray = byteArray.plus(it) }
        return byteArray
    }
}
