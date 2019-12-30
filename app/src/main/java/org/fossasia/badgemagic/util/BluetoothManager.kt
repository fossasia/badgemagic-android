package org.fossasia.badgemagic.util

import android.bluetooth.BluetoothManager
import android.content.Context

class BluetoothManager(val context: Context) {
    private val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val btAdapter = btManager.adapter!!
}
