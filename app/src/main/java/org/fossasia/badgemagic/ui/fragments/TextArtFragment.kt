package org.fossasia.badgemagic.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlinx.android.synthetic.main.effects_layout.*
import kotlinx.android.synthetic.main.fragment_main_textart.*
import kotlinx.android.synthetic.main.sections_tab.*
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.adapter.DrawableAdapter
import org.fossasia.badgemagic.adapter.ModeAdapter
import org.fossasia.badgemagic.adapter.OnDrawableSelected
import org.fossasia.badgemagic.adapter.OnModeSelected
import org.fossasia.badgemagic.core.android.ext.hideKeyboard
import org.fossasia.badgemagic.core.android.ext.showKeyboard
import org.fossasia.badgemagic.data.DrawableInfo
import org.fossasia.badgemagic.data.ModeInfo
import org.fossasia.badgemagic.data.device.model.DataToSend
import org.fossasia.badgemagic.data.device.model.Message
import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed
import org.fossasia.badgemagic.text.CenteredImageSpan
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.ui.custom.knob.Croller
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.DRAWABLE_END
import org.fossasia.badgemagic.util.DRAWABLE_START
import org.fossasia.badgemagic.util.ImageUtils
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.viewmodels.TextArtViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.droidsonroids.gif.GifImageView

class TextArtFragment : BaseFragment() {
    companion object {
        private const val SCAN_TIMEOUT_MS = 9500L
        private const val REQUEST_PERMISSION_CODE = 10
        @JvmStatic
        fun newInstance() =
                TextArtFragment()
    }

    private val drawableRecyclerAdapter = DrawableAdapter()
    private val modeAdapter = ModeAdapter()

    private val viewModel by sharedViewModel<TextArtViewModel>()
    private val bluetoothManager: org.fossasia.badgemagic.util.BluetoothManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        configureEffects()
        setupSpeedKnob()
        setupTabLayout()
        setupTextArtSection()
        setupButton()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_textart, container, false)
    }

    override fun getSendData(): DataToSend {
        textViewMainText.hideKeyboard()
        return SendingUtils.convertToDeviceDataModel(
                Message(
                        Converters.convertEditableToLEDHex(textViewMainText.text.toString(), invertLED.isChecked, viewModel.getClipArts().value
                                ?: SparseArray()),
                        flash.isChecked,
                        marquee.isChecked,
                        Speed.values()[speedKnob.progress.minus(1)],
                        Mode.values()[modeAdapter.getSelectedItemPosition()]
                )
        )
    }

    override fun initializePreview() {
        setPreview()
    }

    private fun setupButton() {
        save_button.setOnClickListener {
            if (checkStoragePermission()) {
                startSaveFile()
            }
        }

        transfer_button.setOnClickListener {
            if (textViewMainText.text.trim().toString() != "") {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    if (scanLocationPermissions()) {
                        // Easter egg
                        Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()

                        transfer_button.visibility = View.GONE
                        send_progress.visibility = View.VISIBLE

                        val buttonTimer = Timer()
                        buttonTimer.schedule(object : TimerTask() {
                            override fun run() {
                                activity?.runOnUiThread {
                                    transfer_button.visibility = View.VISIBLE
                                    send_progress.visibility = View.GONE
                                }
                            }
                        }, SCAN_TIMEOUT_MS)

                        SendingUtils.sendMessage(requireContext(), getSendData())
                    }
                } else
                    showAlertDialog()
            } else
                Toast.makeText(requireContext(), getString(R.string.empty_text_to_send), Toast.LENGTH_LONG).show()
        }
    }

    private fun scanLocationPermissions(): Boolean {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE)
        if (lm is LocationManager) {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder(requireContext())
                        .setMessage(R.string.no_gps_enabled)
                        .setPositiveButton("OK") { _, _ -> requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                        .setNegativeButton("Cancel", null)
                        .show()
                return false
            }
            return true
        }
        return false
    }

    private fun startSaveFile() {
        textViewMainText.hideKeyboard()
        showSaveFileDialog()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startSaveFile()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            false
        } else
            true
    }

    private fun showAlertDialog() {
        val dialogMessage = getString(R.string.enable_bluetooth)
        val builder = AlertDialog.Builder(requireContext())
        builder.setIcon(resources.getDrawable(R.drawable.ic_caution))
        builder.setTitle(getString(R.string.permission_required))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            turnOnBluetooth()
            Toast.makeText(context, R.string.bluetooth_enabled, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(context, R.string.enable_bluetooth, Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    private fun turnOnBluetooth() {
        if (bluetoothManager.btAdapter.disable()) {
            bluetoothManager.btAdapter.enable()
        }
    }

    private val textChangedListener = object : TextWatcher {
        var startPos = -1
        var endPos = -1
        override fun afterTextChanged(s: Editable?) {
            if (startPos != -1)
                s?.delete(startPos, endPos)
            setPreview()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var counter = 0
            endPos = start
            for (i in s.toString().indices) {
                if (s?.get(i) == DRAWABLE_START) {
                    counter++
                    if (startPos == -1)
                        startPos = i
                } else if (s?.get(i) == DRAWABLE_END)
                    counter--

                if (counter == 0) {
                    startPos = -1
                }
            }

            if (startPos != -1)
                viewModel.text = s.toString().removeRange(startPos, start)
            else
                viewModel.text = s.toString()
        }
    }

    private fun setupTextArtSection() {
        textViewMainText.requestFocus()
        textViewMainText.showKeyboard()

        textViewMainText.setText(viewModel.text)

        clipart_handler_layout.setOnClickListener {
            viewModel.showClipart = !viewModel.showClipart
            toggleEmojiSection()
        }
        toggleEmojiSection()
    }

    private fun toggleEmojiSection() {
        viewModel.showClipart.let {
            clipart_layout.visibility = if (it) View.VISIBLE else View.GONE
            clipart_handler.setImageResource(
                    if (it)
                        R.drawable.ic_clipart_switcher_enabled
                    else
                        R.drawable.ic_clipart_switcher_disabled
            )
            if (it) textViewMainText.hideKeyboard() else textViewMainText.showKeyboard()
        }
    }

    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            when (tab?.text) {
                getString(R.string.speed) -> {
                    speedLayout.visibility = View.VISIBLE
                    modeRecyclerView.visibility = View.GONE
                    effectsLayout.visibility = View.GONE
                    viewModel.currentTab = 1
                }
                getString(R.string.mode) -> {
                    speedLayout.visibility = View.GONE
                    modeRecyclerView.visibility = View.VISIBLE
                    effectsLayout.visibility = View.GONE
                    viewModel.currentTab = 2
                }
                getString(R.string.effects) -> {
                    speedLayout.visibility = View.GONE
                    modeRecyclerView.visibility = View.GONE
                    effectsLayout.visibility = View.VISIBLE
                    viewModel.currentTab = 3
                }
            }
        }
    }

    private fun setupTabLayout() {
        val speedTab = tabLayout.newTab().setText(getString(R.string.speed))
        val modeTab = tabLayout.newTab().setText(getString(R.string.mode))
        val effectsTab = tabLayout.newTab().setText(getString(R.string.effects))

        tabLayout.addTab(speedTab)
        tabLayout.addTab(modeTab)
        tabLayout.addTab(effectsTab)
        tabLayout.selectTab(when (viewModel.currentTab) {
            1 -> speedTab
            2 -> modeTab
            else -> effectsTab
        }, true)
    }

    override fun onStart() {
        super.onStart()
        textViewMainText.addTextChangedListener(textChangedListener)
        tabLayout.addOnTabSelectedListener(tabSelectedListener)
    }

    override fun onStop() {
        super.onStop()
        textViewMainText.removeTextChangedListener(textChangedListener)
        tabLayout.removeOnTabSelectedListener(tabSelectedListener)
    }

    private fun setupSpeedKnob() {
        speedKnob.progress = viewModel.speed
        speedKnob.setOnProgressChangedListener(object : Croller.OnProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                textSpeed.text = progress.toString()
                viewModel.speed = progress
                setPreview()
            }
        })
    }

    private fun configureEffects() {
        card_effect_flash.setOnClickListener {
            flash.isChecked = !flash.isChecked
            viewModel.isFlash = flash.isChecked
            setBackgroundOf(card_effect_flash, effect_flash, flash_title, flash.isChecked)
            setPreview()
        }
        card_effect_marquee.setOnClickListener {
            marquee.isChecked = !marquee.isChecked
            viewModel.isMarquee = marquee.isChecked
            setBackgroundOf(card_effect_marquee, effect_marquee, marquee_title, marquee.isChecked)
            setPreview()
        }
        card_effect_invertLED.setOnClickListener {
            invertLED.isChecked = !invertLED.isChecked
            viewModel.isInverted = invertLED.isChecked
            setBackgroundOf(card_effect_invertLED, effect_invertLED, invertLED_title, invertLED.isChecked)
            setPreview()
        }
        setBackgroundOf(card_effect_flash, effect_flash, flash_title, viewModel.isFlash)
        setBackgroundOf(card_effect_marquee, effect_marquee, marquee_title, viewModel.isMarquee)
        setBackgroundOf(card_effect_invertLED, effect_invertLED, invertLED_title, viewModel.isInverted)
        marquee.isChecked = viewModel.isMarquee
        flash.isChecked = viewModel.isFlash
        invertLED.isChecked = viewModel.isInverted
    }

    private fun setBackgroundOf(card: LinearLayout?, image: GifImageView?, title: TextView?, checked: Boolean) {
        card?.background = if (checked) context?.resources?.getDrawable(R.color.colorAccent) else context?.resources?.getDrawable(android.R.color.transparent)
        image?.setColorFilter((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
        title?.setTextColor((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
    }

    private fun setupRecyclerViews() {
        drawablesRecyclerView.layoutManager = GridLayoutManager(context, 9)
        drawablesRecyclerView.adapter = null

        val drawableListener = object : OnDrawableSelected {
            override fun onSelected(selectedItem: DrawableInfo) {
                putBitmapInEditText(selectedItem)
            }
        }
        drawableRecyclerAdapter.apply {
            onDrawableSelected = drawableListener
        }

        viewModel.getClipArts().observe(viewLifecycleOwner, Observer {
            val listOfDrawable = mutableListOf<DrawableInfo>()
            for (i in 0 until it.size()) {
                val key = it.keyAt(i)
                val obj = it.get(key)
                listOfDrawable.add(DrawableInfo(key, obj))
            }

            drawableRecyclerAdapter.addAll(listOfDrawable)
        })

        drawablesRecyclerView.adapter = drawableRecyclerAdapter

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val gridManager = GridLayoutManager(context, 20)
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((position + 1) > 5) 5 else 4
                }
            }
            modeRecyclerView.layoutManager = gridManager
        } else {
            modeRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        modeRecyclerView.adapter = null

        val listOfAnimations = listOf(
                ModeInfo(R.drawable.ic_anim_left, Mode.LEFT),
                ModeInfo(R.drawable.ic_anim_right, Mode.RIGHT),
                ModeInfo(R.drawable.ic_anim_up, Mode.UP),
                ModeInfo(R.drawable.ic_anim_down, Mode.DOWN),
                ModeInfo(R.drawable.ic_anim_fixed, Mode.FIXED),
                ModeInfo(R.drawable.ic_anim_fixed, Mode.SNOWFLAKE),
                ModeInfo(R.drawable.ic_anim_picture, Mode.PICTURE),
                ModeInfo(R.drawable.ic_anim_animation, Mode.ANIMATION),
                ModeInfo(R.drawable.ic_anim_laser, Mode.LASER)
        )
        modeAdapter.addAll(listOfAnimations)
        modeAdapter.setSelectedAnimationPosition(viewModel.animationPosition)
        val modeListener = object : OnModeSelected {
            override fun onSelected(position: Int) {
                viewModel.animationPosition = position
                modeAdapter.setSelectedAnimationPosition(position)
                modeAdapter.notifyDataSetChanged()
                setPreview()
            }
        }
        modeAdapter.apply {
            onModeSelected = modeListener
        }
        modeRecyclerView.adapter = modeAdapter
    }

    private fun putBitmapInEditText(drawableInfo: DrawableInfo) {
        val strToAppend = "«${drawableInfo.id}»"
        val spanStringBuilder = SpannableStringBuilder(strToAppend)
        if (drawableInfo.image is VectorDrawable)
            spanStringBuilder.setSpan(CenteredImageSpan(requireContext(), ImageUtils.trim(ImageUtils.vectorToBitmap(drawableInfo.image), 70)), 0, strToAppend.length, 33)
        else if (drawableInfo.image is BitmapDrawable)
            spanStringBuilder.setSpan(CenteredImageSpan(requireContext(), ImageUtils.trim((drawableInfo.image).bitmap, 70)), 0, strToAppend.length, 33)

        val editable = textViewMainText.text
        val n = textViewMainText.selectionEnd
        if (n < editable.length) {
            editable.insert(n, spanStringBuilder)
            return
        }
        editable.append(spanStringBuilder)
    }

    override fun onResume() {
        super.onResume()
        textViewMainText.requestFocus()
        textViewMainText.showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        textViewMainText.hideKeyboard()
    }

    private fun setPreview() {
        preview_badge.setValue(
                Converters.convertEditableToLEDHex(textViewMainText.text.toString(), invertLED.isChecked, viewModel.getClipArts().value
                        ?: SparseArray()),
                marquee.isChecked,
                flash.isChecked,
                Speed.values()[speedKnob.progress.minus(1)],
                Mode.values()[modeAdapter.getSelectedItemPosition()]
        )
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Calendar.getInstance().time)
    }

    @SuppressLint("InflateParams")
    private fun showSaveFileDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_save, null)
        val saveFileEditText = view.findViewById(R.id.editText) as EditText
        saveFileEditText.setText(getCurrentDate())

        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.save_dialog_title))
                .setView(view)
                .setPositiveButton(R.string.save_button, null)
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val fileTitle = saveFileEditText.text
                if (fileTitle.isEmpty()) {
                    saveFileEditText.error = getString(R.string.validation_save_dialog)
                } else {
                    alertDialog.dismiss()
                    if (viewModel.checkIfFilePresent(fileTitle.toString())) {
                        showFileOverrideDialog(fileTitle.toString(), configToJSON())
                    } else {
                        saveFile(fileTitle.toString(), configToJSON())
                        Toast.makeText(context, R.string.saved_badge, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        alertDialog.show()
        saveFileEditText.requestFocus()
        saveFileEditText.selectAll()
        saveFileEditText.showKeyboard()
    }

    private fun configToJSON(): String {
        return SendingUtils.configToJSON(
                Message(
                        Converters.convertEditableToLEDHex(textViewMainText.text.toString(), false, viewModel.getClipArts().value
                                ?: SparseArray()),
                        flash.isChecked,
                        marquee.isChecked,
                        Speed.values()[speedKnob.progress.minus(1)],
                        Mode.values()[modeAdapter.getSelectedItemPosition()]
                ),
                invertLED.isChecked
        )
    }

    private fun showFileOverrideDialog(fileName: String, jsonString: String) {
        AlertDialog.Builder(context)
                .setTitle(context?.getString(R.string.save_dialog_already_present))
                .setMessage(context?.getString(R.string.save_dialog_already_present_override))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                    saveFile(fileName, jsonString)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }

    private fun saveFile(fileName: String, jsonString: String) {
        viewModel.let { StoreAsync(fileName, jsonString, it).execute() }
    }

    class StoreAsync(private val filename: String, private val json: String, private val viewModel: TextArtViewModel) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            viewModel.saveFile(filename, json)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            viewModel.updateList()
        }
    }
}
