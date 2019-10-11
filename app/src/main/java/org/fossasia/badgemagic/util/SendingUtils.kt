package org.fossasia.badgemagic.util

import android.content.Context
import android.widget.Toast
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.core.android.log.Timber
import org.fossasia.badgemagic.core.utils.ByteArrayUtils
import org.fossasia.badgemagic.data.device.DataToByteArrayConverter
import org.fossasia.badgemagic.data.device.bluetooth.GattClient
import org.fossasia.badgemagic.data.device.bluetooth.ScanHelper
import org.fossasia.badgemagic.data.device.model.DataToSend
import org.fossasia.badgemagic.data.device.model.Message
import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed
import org.fossasia.badgemagic.data.fragments.BadgeConfig

object SendingUtils {

    private val scanHelper = ScanHelper()
    private val gattClient = GattClient()

    fun sendMessage(context: Context, dataToSend: DataToSend) {
        Timber.i { "About to send data: $dataToSend" }
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
                Timber.e { "Scan could not find any device" }
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

    fun returnDefaultMessage(): DataToSend {
        return DataToSend(listOf(Message(
            Converters.convertTextToLEDHex(
                " ",
                false
            ).second,
            false,
            false,
            Speed.ONE,
            Mode.LEFT
        )))
    }

    fun returnMessageWithJSON(badgeJSON: String): DataToSend {
        val badgeConfig = getBadgeFromJSON(badgeJSON)
        return DataToSend(listOf(Message(
            Converters.fixLEDHex(badgeConfig?.hexStrings ?: listOf(), badgeConfig?.isInverted
                ?: false),
            badgeConfig?.isMarquee ?: false,
            badgeConfig?.isFlash ?: false,
            badgeConfig?.speed ?: Speed.ONE,
            badgeConfig?.mode ?: Mode.LEFT
        )))
    }

    fun configToJSON(data: Message, invertLED: Boolean): String {
        val bConfig = BadgeConfig()
        bConfig.hexStrings = data.hexStrings
        bConfig.isFlash = data.flash
        bConfig.isMarquee = data.marquee
        bConfig.isInverted = invertLED
        bConfig.mode = data.mode
        bConfig.speed = data.speed

        return MoshiUtils.getAdapter().toJson(bConfig)
    }

    internal fun getBadgeFromJSON(json: String): BadgeConfig? = MoshiUtils.getAdapter().fromJson(json)
}
