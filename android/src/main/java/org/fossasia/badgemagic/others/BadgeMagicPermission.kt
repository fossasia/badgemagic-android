package org.fossasia.badgemagic.others

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import androidx.core.content.ContextCompat

class BadgeMagicPermission private constructor() {

    companion object {
        private const val REQUEST_PERMISSION_CODE = 10
        val instance: BadgeMagicPermission by lazy { BadgeMagicPermission() }
    }

    val allPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
    )

    val storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
    )

    val ALL_PERMISSION = 100
    val STORAGE_PERMISSION = 101
    val LOCATION_PERMISSION = 102
    val BLUETOOTH_PERMISSION = 103

    val listPermissionsNeeded = arrayListOf<String>()

    fun checkPermissions(activity: Activity, mode: Int) {
        listPermissionsNeeded.clear()
        if (mode == ALL_PERMISSION) {
            for (permission in allPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission)
                }
            }
        }
        if (mode == STORAGE_PERMISSION) {
            for (permission in storagePermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission)
                }
            }
        }
        if (mode == LOCATION_PERMISSION) {
            for (permission in locationPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission)
                }
            }
        }
        if (mode == BLUETOOTH_PERMISSION) {
            for (permission in bluetoothPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission)
                }
            }
        }
        if (listPermissionsNeeded.size > 0) {
            for (permission in listPermissionsNeeded) {
                if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Location Permission Disclosure")
                    builder.setMessage("Badge Magic requires access to location data to enable the transfer of data to LED Badges via Bluetooth LE.")
                    builder.setCancelable(false)
                    builder.setPositiveButton("ACCEPT") { _, _ ->
                        activity.requestPermissions(locationPermissions, REQUEST_PERMISSION_CODE)
                    }
                    builder.setNegativeButton("DENY") { _, _ ->
                        Toast.makeText(activity, "Please grant the permission", Toast.LENGTH_SHORT).show()
                        activity.requestPermissions(locationPermissions, REQUEST_PERMISSION_CODE)
                    }
                    builder.show()
                } else if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Storage Permission Disclosure")
                    builder.setMessage("Badge Magic requires access to storage to enable the storage and import of data.")
                    builder.setCancelable(false)
                    builder.setPositiveButton("ACCEPT") { _, _ ->
                        activity.requestPermissions(storagePermissions, REQUEST_PERMISSION_CODE)
                    }
                    builder.setNegativeButton("DENY") { _, _ ->
                        Toast.makeText(activity, "Please grant the permission", Toast.LENGTH_SHORT).show()
                        activity.requestPermissions(storagePermissions, REQUEST_PERMISSION_CODE)
                    }
                    builder.show()
                } else if (permission == Manifest.permission.BLUETOOTH || permission == Manifest.permission.BLUETOOTH_ADMIN || permission == Manifest.permission.BLUETOOTH_PRIVILEGED || permission == Manifest.permission.BLUETOOTH_CONNECT || permission == Manifest.permission.BLUETOOTH_SCAN) {
                    activity.requestPermissions(bluetoothPermissions, REQUEST_PERMISSION_CODE)
                }
            }
        }
    }
}
