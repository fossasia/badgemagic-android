package org.fossasia.badgemagic.util

import android.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.fossasia.badgemagic.R

class BluetoothAdapter(appContext: Context) {
    private val btManager = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val btAdapter = btManager.adapter!!

    fun isTurnedOn(context: Context): Boolean = when {
        btAdapter.isEnabled -> scanLocationPermissions(context)
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

    private fun scanLocationPermissions(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE)
        if (lm is LocationManager) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder(context)
                    .setMessage(R.string.no_gps_enabled)
                    .setPositiveButton("OK") { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                    .setNegativeButton("Cancel", null)
                    .show()
                return false
            }
            return true
        }
        return false
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
