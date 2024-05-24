package org.fossasia.badgemagic.util

import android.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.fossasia.badgemagic.R

class BluetoothAdapter(appContext: Context) {
    private val btManager = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val btAdapter = btManager.adapter!!

    fun isTurnedOn(context: Context): Boolean = when {
        btAdapter.isEnabled -> true
        else -> {
            showAlertDialog(context)
            false
        }
    }

    private fun turnBluetoothOn() {
        if (btAdapter.disable()) {
            btAdapter.enable()
        }
    }

    private fun showAlertDialog(context: Context) {
        val dialogMessage = context.getString(R.string.enable_bluetooth)
        val builder = AlertDialog.Builder(context)
        builder.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_caution))
        builder.setTitle(context.getString(R.string.permission_required))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            turnBluetoothOn()
            Toast.makeText(context, R.string.bluetooth_enabled, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(context, R.string.enable_bluetooth, Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }
}
