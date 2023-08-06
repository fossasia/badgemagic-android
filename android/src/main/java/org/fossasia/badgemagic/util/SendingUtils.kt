package org.fossasia.badgemagic.util

import android.content.Context
import android.widget.Toast
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.core.android.log.Timber
import org.fossasia.badgemagic.core.bluetooth.GattClient
import org.fossasia.badgemagic.core.bluetooth.ScanHelper
import org.fossasia.badgemagic.data.BadgeConfig
import org.fossasia.badgemagic.data.DataToSend
import org.fossasia.badgemagic.data.Message
import org.fossasia.badgemagic.data.Mode
import org.fossasia.badgemagic.data.Speed
import org.fossasia.badgemagic.device.DataToByteArrayConverter
import org.fossasia.badgemagic.helpers.JSONHelper
import org.fossasia.badgemagic.utils.ByteArrayUtils

object SendingUtils {

    private val scanHelper = ScanHelper()
    private val gattClient = GattClient()

    fun sendMessage(context: Context, dataToSend: DataToSend) {
        Timber.i { "About to send org.fossasia.badgemagic.data: $dataToSend" }
        val byteData = DataToByteArrayConverter.convert(dataToSend)
        sendBytes(context, byteData)
    }

    fun onPause() {
        scanHelper.stopLeScan()
        gattClient.stopClient()
    }

    private fun sendBytes(context: Context, byteData: List<ByteArray>) {
        Timber.i { "ByteData: ${byteData.map { ByteArrayUtils.byteArrayToHexString(it) }}" }

        scanHelper.startLeScan { device ->
            if (device == null) {
                Timber.e { "Scan could not find any org.fossasia.badgemagic.device" }
                Toast.makeText(context, R.string.no_device_found, Toast.LENGTH_SHORT).show()
            } else {
                Timber.e { "Device found: $device" }

                gattClient.startClient(context, device.address) { onConnected ->
                    if (onConnected) {
                        gattClient.writeDataStart(byteData) {
                            Timber.i { "Data sent" }
                            gattClient.stopClient()
                        }
                    } else {
                        Toast.makeText(context, R.string.no_device_found, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun convertToDeviceDataModel(message: Message): DataToSend {
        return DataToSend(listOf(message))
    }

    fun convertToDeviceDataModel(messages: List<Message>): DataToSend {
        return DataToSend(messages)
    }

    fun returnDefaultMessage(): Message {
        return Message(
            Converters.convertTextToLEDHex(
                " ",
                false
            ).second,
            flash = false,
            marquee = false,
            speed = Speed.ONE,
            mode = Mode.LEFT
        )
    }

    fun returnMessageWithJSON(badgeJSON: String): Message {
        val badgeConfig = getBadgeFromJSON(badgeJSON)
        return Message(
            Converters.fixLEDHex(badgeConfig.hexStrings, badgeConfig.isInverted),
            badgeConfig.isFlash,
            badgeConfig.isMarquee,
            badgeConfig.speed,
            badgeConfig.mode
        )
    }

    fun configToJSON(data: Message, invertLED: Boolean): String {
        val bConfig = BadgeConfig()
        bConfig.hexStrings = data.hexStrings
        bConfig.isFlash = data.flash
        bConfig.isMarquee = data.marquee
        bConfig.isInverted = invertLED
        bConfig.mode = data.mode
        bConfig.speed = data.speed

        return JSONHelper.encodeJSON(bConfig)
    }

    internal fun getBadgeFromJSON(json: String): BadgeConfig = JSONHelper.decodeJSON(json)
}
