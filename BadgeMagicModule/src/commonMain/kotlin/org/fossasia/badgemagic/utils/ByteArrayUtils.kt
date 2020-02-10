package org.fossasia.badgemagic.utils

object ByteArrayUtils {

    fun hexStringToByteArray(hexString: String): ByteArray {
        val length = hexString.length
        val data = ByteArray(length / 2)

        for (i in 0 until length step 2) {
            data[i / 2] = ((getCharacterDigit(hexString[i], 16) shl 4) + getCharacterDigit(hexString[i + 1], 16)).toByte()
        }
        return data
    }

    private fun getCharacterDigit(hexChar: Char, radix: Int): Int = hexChar.toString().toInt(radix)

    fun byteArrayToHexString(byteArray: ByteArray): String {
        val builder = StringBuilder()
        for (b in byteArray) {
            builder.append(formatStringToHex(b))
        }
        return builder.toString()
    }

    private fun formatStringToHex(byte: Byte): String = (0xFF and byte.toInt()).toString(16).padStart(2, '0')
}
