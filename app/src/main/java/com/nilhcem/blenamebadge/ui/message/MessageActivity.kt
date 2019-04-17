package com.nilhcem.blenamebadge.ui.message

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Button
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.DrawableAdapter
import com.nilhcem.blenamebadge.adapter.OnDrawableSelected
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.core.android.viewbinding.bindView
import com.nilhcem.blenamebadge.core.android.ext.hideKeyboard
import com.nilhcem.blenamebadge.core.android.ext.showKeyboard
import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import com.nilhcem.blenamebadge.ui.badge_preview.PreviewBadge
import com.nilhcem.blenamebadge.util.Converters
import kotlinx.android.synthetic.main.message_activity.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
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
    private val invertLED: CheckBox by bindView(R.id.invertLED)
    private val speed: Spinner by bindView(R.id.speed)
    private val mode: Spinner by bindView(R.id.mode)
    private val send: Button by bindView(R.id.send_button)
    private val drawableRecyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val sendByteLoader: ProgressBar by bindView(R.id.sendBytesLoader)
    private val radioText: RadioButton by bindView(R.id.textRadio)
    private val radioDrawable: RadioButton by bindView(R.id.drawableRadio)
    private val textSection: LinearLayout by bindView(R.id.section_text)
    private val drawablesSection: LinearLayout by bindView(R.id.section_drawables)
    private val previewBadge: PreviewBadge by bindView(R.id.preview_badge)
    private val save: Button by bindView(R.id.save_button)

    private lateinit var drawableRecyclerAdapter: DrawableAdapter
    private lateinit var moshiConverter: MoshiConverter<String>

    private var selectedID = -1

    private val presenter by lazy { MessagePresenter() }

    private val textWatcherText: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            selectText()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)

        val spinnerItem = R.layout.spinner_item
        speed.adapter = ArrayAdapter<String>(this, spinnerItem, Speed.values().mapIndexed { index, _ -> (index + 1).toString() })
        mode.adapter = ArrayAdapter<String>(this, spinnerItem, Mode.values().map { getString(it.stringResId) })

        send.setOnClickListener {
            content.hideKeyboard()

            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                // Easter egg
                send.isEnabled = false
                val buttonTimer = Timer()
                buttonTimer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            send.isEnabled = true
                            showLoaderView(false)
                        }
                    }
                }, SCAN_TIMEOUT_MS)

                presenter.sendMessage(this,
                        when (selectedID) {
                            R.id.textRadio -> convertTextToDeviceDataModel()
                            else -> convertBitmapToDeviceDataModel()
                        }
                )
            } else {
                prepareForScan()
            }
        }

        save.setOnClickListener {
            try {
                val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                inputManager.hideSoftInputFromWindow(content.windowToken, InputMethodManager.SHOW_FORCED)

                val sdf = SimpleDateFormat("dd-MMM-yyyy-hh_mm_aaa")
                val fileName = "badge-magic-" + sdf.format(Calendar.getInstance().time).toString()
                val os = FileOutputStream(fileName, true)
                var text = "null"
                var drawableID = "null"
                var flashSelected = false
                var marqueeSelected = false

                if (content.text.isNotBlank())
                    text = content.text.toString()
                else if (drawableRecyclerAdapter.selectedPosition >= 0)
                    drawableID = drawableRecyclerAdapter.selectedPosition.toString()

                val selectedSpeed = speed.selectedItem.toString()
                val selectedMode = mode.selectedItem.toString()

                when {
                    flash.isChecked -> flashSelected = true
                    marquee.isChecked -> marqueeSelected = true
                }

                moshiConverter.toStream((text + drawableID + selectedSpeed + selectedMode + flashSelected.toString() + marqueeSelected.toString()),os)

                val sb = StringBuilder()
                sb.append("Records saved successfully with file name ")
                sb.append(fileName)

                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            when (optionId) {
                R.id.drawableRadio -> {
                    content.hideKeyboard()

                    drawablesSection.visibility = View.VISIBLE
                    textSection.visibility = View.GONE
                    selectDrawable(drawableRecyclerAdapter.getSelectedItem())

                    removeListeners()
                    drawableRecyclerAdapter.setListener(object : OnDrawableSelected {
                        override fun onSelected(selectedItem: DrawableInfo?) {
                            selectDrawable(selectedItem)
                        }
                    })
                }

                R.id.textRadio -> {
                    content.requestFocus()
                    content.showKeyboard()

                    textSection.visibility = View.VISIBLE
                    drawablesSection.visibility = View.GONE
                    selectText()
                    removeListeners()
                    content.addTextChangedListener(textWatcherText)
                }
            }
            selectedID = optionId
        }

        val previewCheckedListener: CompoundButton.OnCheckedChangeListener =
                CompoundButton.OnCheckedChangeListener { _, _ -> setPreview() }
        flash.setOnCheckedChangeListener(previewCheckedListener)
        marquee.setOnCheckedChangeListener(previewCheckedListener)
        invertLED.setOnCheckedChangeListener(previewCheckedListener)

        val previewItemListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setPreview()
            }
        }
        speed.onItemSelectedListener = previewItemListener
        mode.onItemSelectedListener = previewItemListener

        radioText.isChecked = true

        setupRecycler()

        prepareForScan()
    }

    fun setPreview() {
        if (radioText.isChecked)
            selectText()
        if (radioDrawable.isChecked)
            selectDrawable(drawableRecyclerAdapter.getSelectedItem())
    }

    private fun removeListeners() {
        content.removeTextChangedListener(textWatcherText)
    }

    fun selectText() {
        val (valid, textToSend) = Converters.convertTextToLEDHex(if (content.text.isNotEmpty()) content.text.toString() else if (!invertLED.isChecked) " " else "", invertLED.isChecked)
        if (!valid) {
            Toast.makeText(baseContext, R.string.character_not_found, Toast.LENGTH_SHORT).show()
        }
        previewBadge.setValue(
                textToSend,
                marquee.isChecked,
                flash.isChecked,
                Speed.values()[speed.selectedItemPosition],
                Mode.values()[mode.selectedItemPosition]
        )
    }

    fun selectDrawable(selectedItem: DrawableInfo?) {
        previewBadge.setValue(
                if (selectedItem != null)
                    Converters.convertDrawableToLEDHex(selectedItem.image, invertLED.isChecked)
                else
                    Converters.convertTextToLEDHex(if (!invertLED.isChecked) " " else "", invertLED.isChecked).second,
                marquee.isChecked,
                flash.isChecked,
                Speed.values()[speed.selectedItemPosition],
                Mode.values()[mode.selectedItemPosition]
        )
    }

    private fun setupRecycler() {
        drawableRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val listOfDrawables = listOf(
                DrawableInfo(resources.getDrawable(R.drawable.apple)),
                DrawableInfo(resources.getDrawable(R.drawable.clock)),
                DrawableInfo(resources.getDrawable(R.drawable.dustbin)),
                DrawableInfo(resources.getDrawable(R.drawable.face)),
                DrawableInfo(resources.getDrawable(R.drawable.heart)),
                DrawableInfo(resources.getDrawable(R.drawable.home)),
                DrawableInfo(resources.getDrawable(R.drawable.invader)),
                DrawableInfo(resources.getDrawable(R.drawable.mail)),
                DrawableInfo(resources.getDrawable(R.drawable.mix1)),
                DrawableInfo(resources.getDrawable(R.drawable.mix2)),
                DrawableInfo(resources.getDrawable(R.drawable.mushroom)),
                DrawableInfo(resources.getDrawable(R.drawable.mustache)),
                DrawableInfo(resources.getDrawable(R.drawable.oneup)),
                DrawableInfo(resources.getDrawable(R.drawable.pause)),
                DrawableInfo(resources.getDrawable(R.drawable.spider)),
                DrawableInfo(resources.getDrawable(R.drawable.sun)),
                DrawableInfo(resources.getDrawable(R.drawable.thumbs_up))
        )

        drawableRecyclerAdapter = DrawableAdapter(this, listOfDrawables)
        drawableRecyclerView.adapter = drawableRecyclerAdapter
    }

    override fun onResume() {
        super.onResume()
        if (selectedID == R.id.textRadio) {
            content.requestFocus()
            content.showKeyboard()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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

    private fun showLoaderView(status: Boolean) {
        runOnUiThread {
            if (status) {
                sendByteLoader.visibility = View.VISIBLE
                send.isEnabled = false
                send.visibility = View.GONE
            } else {
                sendByteLoader.visibility = View.GONE
                send.isEnabled = true
                send.visibility = View.VISIBLE
            }
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
        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(this, R.string.enable_bluetooth, Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    private fun convertTextToDeviceDataModel(): DataToSend {
        return DataToSend(listOf(Message(
                Converters.convertTextToLEDHex(
                        if (content.text.isNotEmpty()) content.text.toString()
                        else if (!invertLED.isChecked) " "
                        else "",
                        invertLED.isChecked
                ).second,
                flash.isChecked, marquee.isChecked,
                Speed.values()[speed.selectedItemPosition],
                Mode.values()[mode.selectedItemPosition]
        )))
    }

    private fun convertBitmapToDeviceDataModel(): DataToSend {
        return DataToSend(listOf(Message(
                Converters.convertDrawableToLEDHex(
                        drawableRecyclerAdapter.getSelectedItem()?.image
                                ?: resources.getDrawable(R.drawable.apple),
                        invertLED.isChecked),
                flash.isChecked,
                marquee.isChecked,
                Speed.values()[speed.selectedItemPosition],
                Mode.values()[mode.selectedItemPosition]
        )))
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
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
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