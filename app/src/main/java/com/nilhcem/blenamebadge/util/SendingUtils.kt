package com.nilhcem.blenamebadge.util

import android.content.Context
import android.widget.Toast
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.core.utils.ByteArrayUtils
import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.data.device.DataToByteArrayConverter
import com.nilhcem.blenamebadge.data.device.bluetooth.GattClient
import com.nilhcem.blenamebadge.data.device.bluetooth.ScanHelper
import com.nilhcem.blenamebadge.data.device.model.DataToSend
import com.nilhcem.blenamebadge.data.device.model.Message
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.data.fragments.BadgeConfig
import com.nilhcem.blenamebadge.data.util.SendingData

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

    fun convertTextToDeviceDataModel(text_to_send: String, data: SendingData): DataToSend {
        return DataToSend(listOf(Message(
            Converters.convertTextToLEDHex(
                if (text_to_send.isNotEmpty()) text_to_send
                else if (!data.invertLED) " "
                else "",
                data.invertLED
            ).second,
            data.flash, data.marquee,
            data.speed,
            data.mode
        )))
    }

    fun convertDrawableToDeviceDataModel(selectedItem: DrawableInfo, data: SendingData): DataToSend {
        return DataToSend(listOf(Message(
            Converters.convertDrawableToLEDHex(
                selectedItem.image,
                data.invertLED),
            data.flash,
            data.marquee,
            data.speed,
            data.mode
        )))
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

    fun configToJSON(selectedID: Int, text_to_send: String, selectedDrawable: DrawableInfo?, data: SendingData): String {
        val bConfig = BadgeConfig()
        bConfig.hexStrings = when (selectedID) {
            R.id.textRadio -> {
                Converters.convertTextToLEDHex(if (text_to_send.isNotEmpty()) text_to_send else " ", false).second
            }
            else -> {
                if (selectedDrawable != null)
                    Converters.convertDrawableToLEDHex(selectedDrawable.image, data.invertLED)
                else
                    Converters.convertTextToLEDHex(" ", false).second
            }
        }
        bConfig.isFlash = data.flash
        bConfig.isMarquee = data.marquee
        bConfig.isInverted = data.invertLED
        bConfig.mode = data.mode
        bConfig.speed = data.speed

        return MoshiUtils.getAdapter().toJson(bConfig)
    }

    internal fun getBadgeFromJSON(json: String): BadgeConfig? = MoshiUtils.getAdapter().fromJson(json)
}