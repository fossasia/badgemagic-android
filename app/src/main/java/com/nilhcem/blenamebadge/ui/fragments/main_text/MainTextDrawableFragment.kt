package com.nilhcem.blenamebadge.ui.fragments.main_text

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.DrawableAdapter
import com.nilhcem.blenamebadge.adapter.ModeAdapter
import com.nilhcem.blenamebadge.adapter.OnDrawableSelected
import com.nilhcem.blenamebadge.adapter.OnModeSelected
import com.nilhcem.blenamebadge.core.android.ext.hideKeyboard
import com.nilhcem.blenamebadge.core.android.ext.showKeyboard
import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.data.ModeInfo
import com.nilhcem.blenamebadge.data.device.model.DataToSend
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.data.util.SendingData
import com.nilhcem.blenamebadge.ui.custom.knob.Croller
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragmentViewModel
import com.nilhcem.blenamebadge.util.Converters
import com.nilhcem.blenamebadge.util.SendingUtils
import kotlinx.android.synthetic.main.effects_layout.*
import kotlinx.android.synthetic.main.fragment_main_text.*
import kotlinx.android.synthetic.main.sections_tab.*
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class MainTextDrawableFragment : BaseFragment(), MainTextDrawableNavigator {
    companion object {
        private const val SCAN_TIMEOUT_MS = 9500L
        @JvmStatic
        fun newInstance() =
            MainTextDrawableFragment()
    }

    private lateinit var drawableRecyclerAdapter: DrawableAdapter
    private lateinit var modeAdapter: ModeAdapter

    private var selectedID = -1

    private val textWatcherText: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            selectText()
        }
    }

    override fun getSendData(): DataToSend {
        text_to_send.hideKeyboard()
        return when (selectedID) {
            R.id.textRadio -> convertTextToDeviceDataModel()
            else -> convertBitmapToDeviceDataModel()
        }
    }

    override fun initializePreview() {
        setPreview()
    }

    override fun selectText() {
        val (valid, textToSend) = Converters.convertTextToLEDHex(if (text_to_send.text.isNotEmpty()) text_to_send.text.toString() else if (!invertLED.isChecked) " " else "", invertLED.isChecked)
        if (!valid) {
            Toast.makeText(context, R.string.character_not_found, Toast.LENGTH_SHORT).show()
        }
        preview_badge.setValue(
            textToSend,
            marquee.isChecked,
            flash.isChecked,
            Speed.values()[speedKnob.progress.minus(1)],
            Mode.values()[modeAdapter.getSelectedItemPosition()]
        )
    }

    override fun selectDrawable(selectedItem: DrawableInfo?) {
        preview_badge.setValue(if (selectedItem != null)
            Converters.convertDrawableToLEDHex(selectedItem.image, invertLED.isChecked)
        else
            Converters.convertTextToLEDHex(if (!invertLED.isChecked) " " else "", invertLED.isChecked).second,
            marquee.isChecked,
            flash.isChecked,
            Speed.values()[speedKnob.progress.minus(1)],
            Mode.values()[modeAdapter.getSelectedItemPosition()]
        )
    }

    override fun convertTextToDeviceDataModel(): DataToSend {
        return SendingUtils.convertTextToDeviceDataModel(
            text_to_send.text.toString(),
            SendingData(
                invertLED.isChecked,
                flash.isChecked,
                marquee.isChecked,
                Mode.values()[modeAdapter.getSelectedItemPosition()],
                Speed.values()[speedKnob.progress.minus(1)]
            )
        )
    }

    override fun convertBitmapToDeviceDataModel(): DataToSend {
        return drawableRecyclerAdapter.getSelectedItem()?.let {
            SendingUtils.convertDrawableToDeviceDataModel(
                it,
                SendingData(
                    invertLED.isChecked,
                    flash.isChecked,
                    marquee.isChecked,
                    Mode.values()[modeAdapter.getSelectedItemPosition()],
                    Speed.values()[speedKnob.progress.minus(1)]
                )
            )
        } ?: DataToSend(listOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        configureEffects()

        setupSpeedKnob()

        setupTabLayout()

        flash_title.isAllCaps = false
        marquee_title.isAllCaps = false
        invertLED_title.isAllCaps = false

        save_button.setOnClickListener {
            text_to_send.hideKeyboard()
            showSaveFileDialog()
        }

        transfer_button.setOnClickListener {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
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
            } else {
                Toast.makeText(requireContext(), getString(R.string.enable_bluetooth), Toast.LENGTH_LONG).show()
            }
        }

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            when (optionId) {
                R.id.drawableRadio -> {
                    text_to_send.hideKeyboard()

                    section_drawables.visibility = View.VISIBLE
                    section_text.visibility = View.GONE
                    selectDrawable(drawableRecyclerAdapter.getSelectedItem())

                    removeListeners()
                    drawableRecyclerAdapter.setListener(object : OnDrawableSelected {
                        override fun onSelected(selectedItem: DrawableInfo?) {
                            selectDrawable(selectedItem)
                        }
                    })
                }

                R.id.textRadio -> {
                    text_to_send.requestFocus()
                    text_to_send.showKeyboard()

                    section_text.visibility = View.VISIBLE
                    section_drawables.visibility = View.GONE
                    selectText()
                    removeListeners()
                    text_to_send.addTextChangedListener(textWatcherText)
                }
            }
            selectedID = optionId
        }

        textRadio.isChecked = true
    }

    override fun setupTabLayout() {
        val speedTab = tabLayout.newTab().setText(requireContext().getString(R.string.speed))
        val modeTab = tabLayout.newTab().setText(requireContext().getString(R.string.mode))
        val effectsTab = tabLayout.newTab().setText(requireContext().getString(R.string.effects))

        tabLayout.addTab(speedTab)
        tabLayout.addTab(modeTab)
        tabLayout.addTab(effectsTab)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    requireContext().getString(R.string.speed) -> {
                        speedLayout.visibility = View.VISIBLE
                        modeRecyclerView.visibility = View.GONE
                        effectsLayout.visibility = View.GONE
                    }
                    requireContext().getString(R.string.mode) -> {
                        speedLayout.visibility = View.GONE
                        modeRecyclerView.visibility = View.VISIBLE
                        effectsLayout.visibility = View.GONE
                    }
                    requireContext().getString(R.string.effects) -> {
                        speedLayout.visibility = View.GONE
                        modeRecyclerView.visibility = View.GONE
                        effectsLayout.visibility = View.VISIBLE
                    }
                }
            }
        })
        tabLayout.selectTab(speedTab, true)
    }

    override fun setupSpeedKnob() {
        speedKnob.setOnProgressChangedListener(object : Croller.OnProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                textSpeed.text = progress.toString()
                setPreview()
            }
        })
    }

    override fun configureEffects() {
        card_effect_flash.setOnClickListener {
            flash.isChecked = !flash.isChecked
            setBackgroundOf(card_effect_flash, effect_flash, flash_title, flash.isChecked)
            setPreview()
        }
        card_effect_marquee.setOnClickListener {
            marquee.isChecked = !marquee.isChecked
            setBackgroundOf(card_effect_marquee, effect_marquee, marquee_title, marquee.isChecked)
            setPreview()
        }
        card_effect_invertLED.setOnClickListener {
            invertLED.isChecked = !invertLED.isChecked
            setBackgroundOf(card_effect_invertLED, effect_invertLED, invertLED_title, invertLED.isChecked)
            setPreview()
        }
    }

    override fun setBackgroundOf(card: LinearLayout?, image: GifImageView?, title: TextView?, checked: Boolean) {
        card?.background = if (checked) context?.resources?.getDrawable(R.color.colorAccent) else context?.resources?.getDrawable(android.R.color.transparent)
        image?.setColorFilter((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
        title?.setTextColor((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
    }

    override fun setupRecyclerViews() {
        drawablesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        drawablesRecyclerView.adapter = null

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

        drawableRecyclerAdapter = DrawableAdapter(context, listOfDrawables)
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

        modeAdapter = ModeAdapter(context, listOfAnimations)
        modeAdapter.setListener(object : OnModeSelected {
            override fun onSelected() {
                setPreview()
            }
        })
        modeRecyclerView.adapter = modeAdapter
    }

    override fun onResume() {
        super.onResume()
        if (selectedID == R.id.textRadio) {
            text_to_send.requestFocus()
            text_to_send.showKeyboard()
        }
    }

    override fun onPause() {
        super.onPause()

        if (selectedID == R.id.textRadio) {
            text_to_send.hideKeyboard()
        }
    }

    override fun removeListeners() {
        text_to_send.removeTextChangedListener(textWatcherText)
    }

    fun setPreview() {
        if (textRadio != null && drawableRadio != null) {
            if (textRadio.isChecked)
                selectText()
            if (drawableRadio.isChecked)
                selectDrawable(drawableRecyclerAdapter.getSelectedItem())
        }
    }

    override fun getCurrentDate(): String {
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Calendar.getInstance().time)
    }

    @SuppressLint("InflateParams")
    override fun showSaveFileDialog() {
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
                    if (viewModel?.checkIfFilePresent(fileTitle.toString()) == true) {
                        showFileOverrideDialog(fileTitle.toString(), configToJSON())
                    } else {
                        saveFile(fileTitle.toString(), configToJSON())
                    }
                }
            }
        }
        alertDialog.show()
        saveFileEditText.requestFocus()
        saveFileEditText.selectAll()
        saveFileEditText.showKeyboard()
    }

    override fun configToJSON(): String {
        return SendingUtils.configToJSON(
            selectedID,
            text_to_send.text.toString(),
            drawableRecyclerAdapter.getSelectedItem(),
            SendingData(
                invertLED.isChecked,
                flash.isChecked,
                marquee.isChecked,
                Mode.values()[modeAdapter.getSelectedItemPosition()],
                Speed.values()[speedKnob.progress.minus(1)]
            )
        )
    }

    override fun showFileOverrideDialog(fileName: String, jsonString: String) {
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

    override fun saveFile(fileName: String, jsonString: String) {
        viewModel?.let { StoreAsync(fileName, jsonString, it).execute() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_text, container, false)
    }

    class StoreAsync(private val filename: String, private val json: String, private val viewModel: BaseFragmentViewModel) : AsyncTask<Void, Void, Void>() {
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