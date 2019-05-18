package org.fossasia.badgemagic.ui.drawer

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
import androidx.lifecycle.ViewModelProviders
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.core.android.log.Timber
import org.fossasia.badgemagic.ui.fragments.base.BaseFragment
import org.fossasia.badgemagic.ui.AppViewModel
import org.fossasia.badgemagic.ui.fragments.AboutFragment
import org.fossasia.badgemagic.ui.fragments.main_saved.MainSavedFragment
import org.fossasia.badgemagic.ui.fragments.main_text.MainTextDrawableFragment
import org.fossasia.badgemagic.util.InjectorUtils
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.util.StorageUtils

@Suppress("DEPRECATION")
class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val PICK_FILE_RESULT_CODE = 2
        private const val REQUEST_PERMISSION_CODE = 10
        private const val REQUEST_ENABLE_BT = 1
    }

    private var showMenu: Menu? = null
    private var drawerCheckedID = R.id.create

    private var viewModel: AppViewModel? = null

    private fun inject() {
        viewModel = ViewModelProviders.of(this, InjectorUtils.provideFilesViewModelFactory())
            .get(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContentView(R.layout.activity_drawer)

        if (intent.action == Intent.ACTION_VIEW)
            importFile(intent)

        setupDrawerAndToolbar()

        prepareForScan()

        if (intent.action == "org.fossasia.badgemagic.savedBadges.shortcut") {
            switchFragment(MainSavedFragment.newInstance())
            showMenu?.setGroupVisible(R.id.saved_group, true)
        }

        if (intent.action == "org.fossasia.badgemagic.createBadge.shortcut") {
            switchFragment(MainTextDrawableFragment.newInstance())
            showMenu?.setGroupVisible(R.id.saved_group, false)
        }
    }

    private fun setupDrawerAndToolbar() {
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
                        switchFragment(AboutFragment.newInstance())
                        showMenu?.setGroupVisible(R.id.saved_group, false)
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

    private fun prepareForScan() {
        if (isBleSupported()) {
            checkManifestPermission()
        } else {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun importFile(data: Intent?) {
        showImportDialog(data?.data)
    }

    private fun showImportDialog(uri: Uri?) {
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

    override fun onPause() {
        super.onPause()
        SendingUtils.onPause()
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

    private fun showAlertDialog(bluetoothDialog: Boolean) {
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

    private fun switchFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frag_container, fragment)
            .commit()
    }

    private fun checkManifestPermission() {
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

    private fun isBleSupported(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    private fun ensureBluetoothEnabled() {
        // Ensures Bluetooth is enabled on the device
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (!btAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun disableBluetooth() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (btAdapter.isEnabled) {
            btAdapter.disable()
        }
    }

    private fun saveImportFile(uri: Uri?) {
        if (StorageUtils.copyFileToDirectory(this, uri)) {
            Toast.makeText(this, R.string.success_import_json, Toast.LENGTH_SHORT).show()
            viewModel?.updateList()
        } else Toast.makeText(this, R.string.invalid_import_json, Toast.LENGTH_SHORT).show()
    }

    private fun showOverrideDialog(uri: Uri?) {
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

    override fun onDestroy() {
        disableBluetooth()
        super.onDestroy()
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
