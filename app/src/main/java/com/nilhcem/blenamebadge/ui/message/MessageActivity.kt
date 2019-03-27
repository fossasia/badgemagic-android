package com.nilhcem.blenamebadge.ui.message

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.core.android.ext.showKeyboard
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.core.android.viewbinding.bindView
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import com.nilhcem.blenamebadge.ui.badge_preview.PreviewBadge
import com.nilhcem.blenamebadge.util.Utils
import java.util.Timer
import java.util.TimerTask

class MessageActivity : AppCompatActivity() {

    companion object {
        private const val SCAN_TIMEOUT_MS = 10_000L
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_PERMISSION_LOCATION = 1
    }

    private val content: EditText by bindView(R.id.text_to_send)
    private val flash: CheckBox by bindView(R.id.flash)
    private val marquee: CheckBox by bindView(R.id.marquee)
    private val speed: Spinner by bindView(R.id.speed)
    private val mode: Spinner by bindView(R.id.mode)
    private val send: Button by bindView(R.id.send_button)
    private val previewButton: Button by bindView(R.id.preview_button)

    private val previewBadge: PreviewBadge by bindView(R.id.preview_badge)

    private val presenter by lazy { MessagePresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)

        val spinnerItem = R.layout.spinner_item
        speed.adapter = ArrayAdapter<String>(this, spinnerItem, Speed.values().mapIndexed { index, _ -> (index + 1).toString() })
        mode.adapter = ArrayAdapter<String>(this, spinnerItem, Mode.values().map { getString(it.stringResId) })

        send.setOnClickListener {
            val inputManager: InputMethodManager = this?.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            inputManager.hideSoftInputFromWindow(content.windowToken, InputMethodManager.SHOW_FORCED)
            if (content.text.isEmpty()) {
                content.isFocusable = true
                content.error = getString(R.string.empty_message)
            } else {
                Utils.hideSoftKeyboard(baseContext, window.decorView)

                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    // Easter egg
                    send.isClickable = false
                    val buttonTimer = Timer()
                    buttonTimer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread { send.isClickable = true }
                        }
                    }, SCAN_TIMEOUT_MS)
                    if (content.text.isEmpty()) {
                        presenter.sendBitmap(this, BitmapFactory.decodeResource(resources, R.drawable.mix2))
                    } else {
                        presenter.sendMessage(this, convertToDeviceDataModel())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.txt_turn_on_bluetooth), Toast.LENGTH_LONG).show()
                }
            }
        }

        content.setOnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (content.text.isEmpty()) {
                    content.isFocusable = true
                    content.error = getString(R.string.empty_message)
                } else {
                    Utils.hideSoftKeyboard(baseContext, window.decorView)
                }
                true
            } else {
                false
            }
        }

        previewButton.setOnClickListener {
            if (!content.text.isEmpty()) {
                previewBadge.setValue(presenter.convertToPreview(content.text.toString()))
            }
        }

        prepareForScan()
    }

    override fun onResume() {
        super.onResume()
        content.requestFocus()
        content.showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showAlertDialog(true)
                return
            } else if (resultCode == Activity.RESULT_OK) {
                prepareForScan()
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.d { "Location permission accepted" }
            } else {
                showAlertDialog(false)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showAlertDialog(bluetoothDialog: Boolean) {
        val dialogMessage = if (bluetoothDialog) getString(R.string.enable_bluetooth) else getString(R.string.grant_location_permission)
        val builder = AlertDialog.Builder(this)
        builder.setIcon(resources.getDrawable(R.drawable.ic_caution))
        builder.setTitle(getString(R.string.permission_required))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            prepareForScan()
        }
        builder.create().show()
    }

    private fun convertToDeviceDataModel(): DataToSend {
        return DataToSend(listOf(Message(content.text.trim().toString(), flash.isChecked, marquee.isChecked, Speed.values()[speed.selectedItemPosition], Mode.values()[mode.selectedItemPosition])))
    }

    private fun prepareForScan() {
        if (isBleSupported()) {
            // Ensures Bluetooth is enabled on the device
            val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val btAdapter = btManager.adapter
            if (btAdapter.isEnabled) {
                // Prompt for runtime permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Timber.i { "Coarse permission granted" }
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSION_LOCATION)
                }
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        } else {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun isBleSupported(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }
}
