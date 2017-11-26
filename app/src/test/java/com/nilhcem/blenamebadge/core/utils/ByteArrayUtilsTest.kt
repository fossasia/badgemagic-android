package com.nilhcem.blenamebadge.core.utils

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`
import org.junit.Test

class ByteArrayUtilsTest {

    @Test
    fun `should convert hex string to byte array`() {
        // Given
        val hexString = "00CAFEBABEDEADBEEFFF"

        // When
        val result = ByteArrayUtils.hexStringToByteArray(hexString)

        // Then
        val expected = listOf((0xCA).toByte(), (0xFE).toByte(), (0xBA).toByte(), (0xBE).toByte(), (0xDE).toByte(), (0xAD).toByte(), (0xBE).toByte(), (0xEF).toByte(), (0xFF).toByte())
        result `should contain all` expected.toByteArray()
    }

    @Test
    fun `should convert byte array to hex string`() {
        // Given
        val byteArray = listOf((0xBA.toByte()), (0xAD.toByte()), (0xF0.toByte()), (0x0D.toByte())).toByteArray()

        // When
        val result = ByteArrayUtils.byteArrayToHexString(byteArray)

        // Then
        result `should be equal to` "BAADF00D"
    }
}
