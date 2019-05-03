package com.nilhcem.blenamebadge.ui.drawer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment
import com.nilhcem.blenamebadge.ui.fragments.main_saved.MainSavedFragment
import com.nilhcem.blenamebadge.ui.fragments.main_text.MainTextDrawableFragment
import com.nilhcem.blenamebadge.util.StorageUtils

@Suppress("DEPRECATION")
class DrawerActivity : AppCompatActivity(), DrawerNavigator, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val PICK_FILE_RESULT_CODE = 2
        private const val REQUEST_PERMISSION_CODE = 10
        private const val REQUEST_ENABLE_BT = 1
    }

    private var showMenu: Menu? = null
    private var drawerCheckedID = R.id.create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        if (intent.action == Intent.ACTION_VIEW)
            importFile(intent)

        setupDrawerAndToolbar()

        prepareForScan()
    }

    override fun setupDrawerAndToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                when (drawerCheckedID) {
                    R.id.create -> {
                        switchFragment(MainTextDrawableFragment.newInstance())
                        showMenu?.setGroupVisible(R.id.saved_group, false)
                    }
                    R.id.saved -> {
                        switchFragment(MainSavedFragment.newInstance())
                        showMenu?.setGroupVisible(R.id.saved_group, true)
                    }
                    R.id.feedback -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fossasia/badge-magic-android/issues")))
                    }
                    R.id.buy -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://sg.pslab.io")))
                    }
                    R.id.about -> {
                    }
                }
            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })

        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.create)
        switchFragment(MainTextDrawableFragment.newInstance())
        showMenu?.setGroupVisible(R.id.saved_group, false)
    }

    override fun prepareForScan() {
        if (isBleSupported()) {
            checkManifestPermission()
        } else {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun importFile(data: Intent?) {
        showImportDialog(data?.data)
    }

    override fun showImportDialog(uri: Uri?) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.import_dialog))
            .setMessage("${getString(R.string.import_dialog_message)} ${StorageUtils.getFileName(this, uri
                ?: Uri.EMPTY)}")
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                if (!StorageUtils.checkIfFilePresent(this, uri)) {
                    saveImportFile(uri)
                } else
                    showOverrideDialog(uri)
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_FILE_RESULT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    importFile(data)
                }
            }
            REQUEST_ENABLE_BT -> {
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                if (resultCode == Activity.RESULT_CANCELED) {
                    showAlertDialog(true)
                    return
                } else if (resultCode == Activity.RESULT_OK) {
                    prepareForScan()
                    return
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showAlertDialog(bluetoothDialog: Boolean) {
        val dialogMessage = if (bluetoothDialog) getString(R.string.enable_bluetooth) else getString(R.string.grant_required_permission)
        val builder = AlertDialog.Builder(this)
        builder.setIcon(resources.getDrawable(R.drawable.ic_caution))
        builder.setTitle(getString(R.string.permission_required))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            prepareForScan()
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(this, R.string.enable_bluetooth, Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    override fun switchFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frag_container, fragment)
            .commit()
    }

    override fun checkManifestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ensureBluetoothEnabled()
            Timber.i { "Coarse permission granted" }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d { "Required Permission Accepted" }
                    ensureBluetoothEnabled()
                } else {
                    showAlertDialog(false)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun isBleSupported(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    override fun ensureBluetoothEnabled() {
        // Ensures Bluetooth is enabled on the device
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (!btAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    override fun saveImportFile(uri: Uri?) {
        if (StorageUtils.copyFileToDirectory(this, uri)) Toast.makeText(this, R.string.success_import_json, Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, R.string.invalid_import_json, Toast.LENGTH_SHORT).show()
    }

    override fun showOverrideDialog(uri: Uri?) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.save_dialog_already_present))
            .setMessage(getString(R.string.save_dialog_already_present_override))
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                saveImportFile(uri)
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        showMenu = menu
        val inflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        menu?.setGroupVisible(R.id.saved_group, false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_import -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "text/plain"
                }
                startActivityForResult(intent, PICK_FILE_RESULT_CODE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerCheckedID = item.itemId
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
