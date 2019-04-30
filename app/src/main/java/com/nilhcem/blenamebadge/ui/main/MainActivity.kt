package com.nilhcem.blenamebadge.ui.main

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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.MainPagerAdapter
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import com.nilhcem.blenamebadge.ui.fragments.BaseFragment
import com.nilhcem.blenamebadge.ui.fragments.MainSavedFragment
import com.nilhcem.blenamebadge.ui.fragments.MainTextDrawableFragment
import com.nilhcem.blenamebadge.ui.interfaces.PreviewChangeListener
import com.nilhcem.blenamebadge.util.StorageUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_text.*
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), PreviewChangeListener {

    companion object {
        private const val SCAN_TIMEOUT_MS = 9500L
        private const val PICK_FILE_RESULT_CODE = 2
        private const val REQUEST_PERMISSION_CODE = 10
        private const val REQUEST_ENABLE_BT = 1
    }

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainPagerAdapter

    private var fragmentList = listOf<BaseFragment>()

    private val presenter by lazy { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.action == Intent.ACTION_VIEW)
            importFile(intent)

        viewPager = findViewById(R.id.viewPager)

        setupBottomNavigationMenu()
        prepareForScan()
    }

    private fun setupFabListener(bluetoothPresent: Boolean) {
        if (bluetoothPresent)
            fab_main.setOnClickListener {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    if (viewPager.currentItem == 0 && textRadio.isChecked && text_to_send.text.isEmpty()) {
                        Toast.makeText(this, getString(R.string.empty_edittext), Toast.LENGTH_SHORT).show()
                    } else {
                        // Easter egg
                        Toast.makeText(this, getString(R.string.sending_data), Toast.LENGTH_LONG).show()
                        startFabAnimation()

                        val buttonTimer = Timer()
                        buttonTimer.schedule(object : TimerTask() {
                            override fun run() {
                                runOnUiThread {
                                    endFabAnimation()
                                }
                            }
                        }, SCAN_TIMEOUT_MS)

                        presenter.sendMessage(this, fragmentList[viewPager.currentItem].getSendData())
                    }
                } else {
                    prepareForScan()
                }
            }
        else
            fab_main.setOnClickListener {
                Toast.makeText(this, getString(R.string.enable_bluetooth), Toast.LENGTH_LONG).show()
            }
    }

    private fun startFabAnimation() {
        fab_main.animate().translationXBy(-200f).withEndAction {
            fab_main.animate().translationXBy(500f).duration = 150
        }
    }

    private fun endFabAnimation() {
        fab_main.animate().translationXBy(-500f).setDuration(150).withEndAction {
            fab_main.animate().translationXBy(200f).duration = 300
        }
    }

    override fun onPreviewChange(hexStrings: List<String>, marquee: Boolean, flash: Boolean, speed: Speed, mode: Mode) {
        preview_badge.setValue(
            hexStrings,
            marquee,
            flash,
            speed,
            mode
        )
    }

    override fun updateSavedList() {
        for (fragment in fragmentList) {
            fragment.updateSavedList()
        }
    }

    private fun setupViewPager() {
        fragmentList = listOf(
            MainTextDrawableFragment.newInstance(),
            MainSavedFragment.newInstance()
        )
        pagerAdapter = MainPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                fragmentList[viewPager.currentItem].initializePreview()
                navigation.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupBottomNavigationMenu() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.textDrawFragment -> viewPager.currentItem = 0
                R.id.savedFragment -> viewPager.currentItem = 1
                else -> viewPager.currentItem = 0
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun prepareForScan() {
        if (isBleSupported()) {
            checkManifestPermission()
        } else {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun ensureBluetoothEnabled() {
        // Ensures Bluetooth is enabled on the device
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (btAdapter.isEnabled) {
            setupFabListener(true)
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
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
                    setupFabListener(false)
                    return
                } else if (resultCode == Activity.RESULT_OK) {
                    prepareForScan()
                    setupFabListener(true)
                    return
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun saveImportFile(uri: Uri?) {
        if (StorageUtils.copyFileToDirectory(this, uri)) updateSavedList()
        else Toast.makeText(this, R.string.invalid_import_json, Toast.LENGTH_SHORT).show()
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
        val inflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d { "Required Permission Accepted" }
                    setupViewPager()
                    ensureBluetoothEnabled()
                } else {
                    showAlertDialog(false)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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

    private fun checkManifestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setupViewPager()
            ensureBluetoothEnabled()
            Timber.i { "Coarse permission granted" }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        }
    }

    private fun isBleSupported(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}