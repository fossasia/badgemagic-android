package com.nilhcem.blenamebadge.ui.drawer

import android.content.Intent
import android.net.Uri
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment

interface DrawerNavigator {

    fun switchFragment(fragment: BaseFragment)

    fun ensureBluetoothEnabled()

    fun prepareForScan()

    fun importFile(data: Intent?)

    fun showImportDialog(uri: Uri?)

    fun saveImportFile(uri: Uri?)

    fun showOverrideDialog(uri: Uri?)

    fun showAlertDialog(bluetoothDialog: Boolean)

    fun checkManifestPermission()

    fun isBleSupported(): Boolean

    fun setupDrawerAndToolbar()

    fun inject()
}
