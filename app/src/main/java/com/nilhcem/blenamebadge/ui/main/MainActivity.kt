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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.MainPagerAdapter
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment
import com.nilhcem.blenamebadge.ui.fragments.main_saved.MainSavedFragment
import com.nilhcem.blenamebadge.ui.fragments.main_text.MainTextDrawableFragment
import com.nilhcem.blenamebadge.util.InjectorUtils
import com.nilhcem.blenamebadge.util.SendingUtils
import com.nilhcem.blenamebadge.util.StorageUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), MainNavigator {

    companion object {
        private const val SCAN_TIMEOUT_MS = 9500L
        private const val PICK_FILE_RESULT_CODE = 2
        private const val REQUEST_PERMISSION_CODE = 10
        private const val REQUEST_ENABLE_BT = 1
    }

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainPagerAdapter

    private var fragmentList = listOf<BaseFragment>()

    private var viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContentView(R.layout.activity_main)

        if (intent.action == Intent.ACTION_VIEW)
            importFile(intent)

        viewPager = findViewById(R.id.viewPager)

        setupBottomNavigationMenu()

        setupPreviewObserver()

        prepareForScan()
    }

    override fun setupPreviewObserver() {
        viewModel?.getPreviewDetails()?.observe(this, Observer { message ->
            preview_badge.setValue(
                message.hexStrings,
                message.marquee,
                message.flash,
                message.speed,
                message.mode
            )
        })
    }

    override fun inject() {
        val savedConfigFactory = InjectorUtils.provideDataViewModelFactory()
        viewModel = ViewModelProviders.of(this, savedConfigFactory)
            .get(MainActivityViewModel::class.java)
    }

    override fun setupFabListener(bluetoothPresent: Boolean) {
        if (bluetoothPresent)
            fab_main.setOnClickListener {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
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

                    SendingUtils.sendMessage(this, fragmentList[viewPager.currentItem].getSendData())
                } else {
                    prepareForScan()
                }
            }
        else
            fab_main.setOnClickListener {
                Toast.makeText(this, getString(R.string.enable_bluetooth), Toast.LENGTH_LONG).show()
            }
    }

    override fun startFabAnimation() {
        fab_main.animate().translationXBy(-200f).withEndAction {
            fab_main.animate().translationXBy(500f).duration = 150
        }
    }

    override fun endFabAnimation() {
        fab_main.animate().translationXBy(-500f).setDuration(150).withEndAction {
            fab_main.animate().translationXBy(200f).duration = 300
        }
    }

    override fun setupViewPager() {
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

    override fun setupBottomNavigationMenu() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.textDrawFragment -> viewPager.currentItem = 0
                R.id.savedFragment -> viewPager.currentItem = 1
                else -> viewPager.currentItem = 0
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun prepareForScan() {
        if (isBleSupported()) {
            checkManifestPermission()
        } else {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun ensureBluetoothEnabled() {
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

    override fun saveImportFile(uri: Uri?) {
        if (StorageUtils.copyFileToDirectory(this, uri)) viewModel?.updateList()
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

    override fun checkManifestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setupViewPager()
            ensureBluetoothEnabled()
            Timber.i { "Coarse permission granted" }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        }
    }

    override fun isBleSupported(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    override fun onPause() {
        super.onPause()
        SendingUtils.onPause()
    }
}