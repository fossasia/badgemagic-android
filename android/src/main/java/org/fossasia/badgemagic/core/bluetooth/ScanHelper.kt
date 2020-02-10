package org.fossasia.badgemagic.core.bluetooth

import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.ParcelUuid
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import org.fossasia.badgemagic.core.android.log.Timber
import org.fossasia.badgemagic.util.BadgeUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class ScanHelper : KoinComponent {

    companion object {
        private const val SCAN_TIMEOUT_MS = 10_000L
    }

    private var isScanning = false
    private var onDeviceFoundCallback: ((BluetoothDevice?) -> Unit)? = null

    private val badgeUtils: BadgeUtils by inject()

    private val scanner by lazy { BluetoothLeScannerCompat.getScanner() }
    private val stopScanHandler = Handler()
    private val stopScanRunnable = Runnable {
        Timber.i { "No devices found" }
        stopLeScan()
        onDeviceFoundCallback?.invoke(null)
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Timber.i { "onScanResult: ${result.device.address}" }
            stopLeScan()
            onDeviceFoundCallback?.invoke(result.device)
        }

        override fun onScanFailed(errorCode: Int) {
            Timber.w { "Scan failed: $errorCode" }
            stopLeScan()
            onDeviceFoundCallback?.invoke(null)
        }
    }

    fun startLeScan(onDeviceFoundCallback: ((BluetoothDevice?) -> Unit)) {
        if (isScanning) return
        this.onDeviceFoundCallback = onDeviceFoundCallback
        isScanning = true

        val filters = listOf(ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(badgeUtils.currentDevice.serviceID))
            .build())

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner.startScan(filters, settings, scanCallback)

        // Stops scanning after a pre-defined scan period.
        stopScanHandler.postDelayed(stopScanRunnable, SCAN_TIMEOUT_MS)
    }

    fun stopLeScan() {
        if (isScanning) {
            isScanning = false

            scanner.stopScan(scanCallback)
            stopScanHandler.removeCallbacks(stopScanRunnable)
        }
    }
}
