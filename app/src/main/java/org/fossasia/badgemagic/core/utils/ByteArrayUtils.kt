package org.fossasia.badgemagic.core.utils

object ByteArrayUtils {

    fun hexStringToByteArray(hexString: String): ByteArray {
        val length = hexString.length
        val data = ByteArray(length / 2)

        for (i in 0 until length step 2) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
        }
        return data
    }

    fun byteArrayToHexString(byteArray: ByteArray): String {
        val builder = StringBuilder()
        for (b in byteArray) {
            builder.append(String.format("%02X", b))
        }
        return builder.toString()
    }
}
