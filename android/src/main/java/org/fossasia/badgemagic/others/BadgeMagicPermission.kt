package org.fossasia.badgemagic.others

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.fossasia.badgemagic.R

class BadgeMagicPermission private constructor() {

    companion object {
        private const val REQUEST_PERMISSION_CODE = 10
        val instance: BadgeMagicPermission by lazy { BadgeMagicPermission() }
    }

    val allPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
    )

    val storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
    )

    val ALL_PERMISSION = 100
    val STORAGE_PERMISSION = 101
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
        if (mode == BLUETOOTH_PERMISSION) {
            for (permission in bluetoothPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission)
                }
            }
        }
        if (listPermissionsNeeded.size > 0) {
            for (permission in listPermissionsNeeded) {
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    AlertDialog.Builder(activity)
                        .setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_caution))
                        .setTitle(activity.getString(R.string.storage_required_title))
                        .setMessage(activity.getString(R.string.storage_required_message))
                        .setPositiveButton("OK") { _, _ ->
                            activity.requestPermissions(storagePermissions, REQUEST_PERMISSION_CODE)
                        }
                        .setNegativeButton("Cancel") { _, _ ->
                            Toast.makeText(activity, activity.getString(R.string.storage_canceled_warning), Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
                } else if (permission == Manifest.permission.BLUETOOTH_CONNECT || permission == Manifest.permission.BLUETOOTH_SCAN) {
                    activity.requestPermissions(bluetoothPermissions, REQUEST_PERMISSION_CODE)
                }
            }
        }
    }
}
