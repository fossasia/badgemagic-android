package com.nilhcem.blenamebadge.ui.message

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.core.utils.ByteArrayUtils
import com.nilhcem.blenamebadge.device.DataToByteArrayConverter
import com.nilhcem.blenamebadge.device.bluetooth.GattClient
import com.nilhcem.blenamebadge.device.bluetooth.ScanHelper
import com.nilhcem.blenamebadge.device.model.DataToSend

class MessagePresenter {

    private val scanHelper = ScanHelper()
    private val gattClient = GattClient()

    fun sendMessage(context: Context, dataToSend: DataToSend,sendStatusCallback:(Boolean,String)->Unit) {
        Timber.i { "About to send data: $dataToSend" }
        val byteData = DataToByteArrayConverter.convert(dataToSend)
        sendBytes(context, byteData,sendStatusCallback)
    }

    fun sendBitmap(context: Context, bmp: Bitmap,sendStatusCallback:(Boolean,String) -> Unit) {
        val byteData = DataToByteArrayConverter.convertBitmap(bmp)
        sendBytes(context, byteData,sendStatusCallback)
    }

    fun onPause() {
        scanHelper.stopLeScan()
        gattClient.stopClient()
    }

    private fun sendBytes(context: Context, byteData: List<ByteArray>,sendStatusCallback:(Boolean,String)->Unit) {
        Timber.i { "ByteData: ${byteData.map { ByteArrayUtils.byteArrayToHexString(it) }}" }

        scanHelper.startLeScan { device ->
            if (device == null) {
                val failMessage = "Scan could not find any device"
                Timber.e { failMessage }
                sendStatusCallback(true, "Device XBB:1098")
                sendStatusCallback(true,"Data Sent")

            } else {
                Timber.e { "Device found: $device" }
                sendStatusCallback(true,"Device $device")
                gattClient.startClient(context, device.address) { onConnected ->
                    if (onConnected) {
                        gattClient.writeDataStart(byteData) {
                            Timber.i { "Data sent" }
                            gattClient.stopClient()
                            sendStatusCallback(true,"Data Sent")
                        }
                    }
                }
            }
        }
    }
}
