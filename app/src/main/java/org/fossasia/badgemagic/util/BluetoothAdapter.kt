package org.fossasia.badgemagic.util

import android.bluetooth.BluetoothManager
import android.content.Context

class BluetoothAdapter(val context: Context) {
    private val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val btAdapter = btManager.adapter!!

    fun isOn(): Boolean = btAdapter.isEnabled

    fun turnBluetoothOn() {
        if (btAdapter.disable()) {
            btAdapter.enable()
        }
    }
}
