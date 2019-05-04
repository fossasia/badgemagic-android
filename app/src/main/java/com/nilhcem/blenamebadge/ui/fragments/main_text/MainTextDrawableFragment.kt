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
import androidx.lifecycle.ViewModelProviders
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
import com.nilhcem.blenamebadge.ui.AppViewModel
import com.nilhcem.blenamebadge.util.Converters
import com.nilhcem.blenamebadge.util.SendingUtils
import kotlinx.android.synthetic.main.effects_layout.view.*
import kotlinx.android.synthetic.main.fragment_main_text.view.*
import kotlinx.android.synthetic.main.sections_tab.view.*
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class MainTextDrawableFragment : BaseFragment() {
    companion object {
        private const val SCAN_TIMEOUT_MS = 9500L
        @JvmStatic
        fun newInstance() =
            MainTextDrawableFragment()
    }

    private val drawableRecyclerAdapter = DrawableAdapter()
    private val modeAdapter = ModeAdapter()
    private lateinit var mainViewModel: MainTextDrawableViewModel
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myActivity = activity
        if (myActivity != null) {
            mainViewModel = ViewModelProviders.of(myActivity).get(MainTextDrawableViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_text, container, false)

        setupRecyclerViews()
        configureEffects()
        setupSpeedKnob()
        setupTabLayout()
        setupTextDrawableSection()
        setupButton()

        return rootView
    }

    override fun getSendData(): DataToSend {
        rootView.text_to_send.hideKeyboard()
        return when (mainViewModel.radioSelectedId) {
            R.id.textRadio -> convertTextToDeviceDataModel()
            else -> convertBitmapToDeviceDataModel()
        }
    }

    override fun initializePreview() {
        setPreview()
    }

    private fun selectText() {
        val (valid, textToSend) = Converters.convertTextToLEDHex(
                if (rootView.text_to_send.text.isNotEmpty()) rootView.text_to_send.text.toString()
                else if (!rootView.invertLED.isChecked) " " else "", rootView.invertLED.isChecked)
        if (!valid) {
            Toast.makeText(context, R.string.character_not_found, Toast.LENGTH_SHORT).show()
        }
        rootView.preview_badge.setValue(
            textToSend,
            rootView.marquee.isChecked,
            rootView.flash.isChecked,
            Speed.values()[rootView.speedKnob.progress.minus(1)],
            Mode.values()[modeAdapter.getSelectedItemPosition()]
        )
    }

    private fun selectDrawable(selectedItem: DrawableInfo?) {
        rootView.preview_badge.setValue(if (selectedItem != null)
            Converters.convertDrawableToLEDHex(selectedItem.image, rootView.invertLED.isChecked)
        else
            Converters.convertTextToLEDHex(if (!rootView.invertLED.isChecked) " " else "", rootView.invertLED.isChecked).second,
            rootView.marquee.isChecked,
            rootView.flash.isChecked,
            Speed.values()[rootView.speedKnob.progress.minus(1)],
            Mode.values()[modeAdapter.getSelectedItemPosition()]
        )
    }

    private fun convertTextToDeviceDataModel(): DataToSend {
        return SendingUtils.convertTextToDeviceDataModel(
            rootView.text_to_send.text.toString(),
            SendingData(
                rootView.invertLED.isChecked,
                rootView.flash.isChecked,
                rootView.marquee.isChecked,
                Mode.values()[modeAdapter.getSelectedItemPosition()],
                Speed.values()[rootView.speedKnob.progress.minus(1)]
            )
        )
    }

    private fun convertBitmapToDeviceDataModel(): DataToSend {
        return drawableRecyclerAdapter.getSelectedItem()?.let {
            SendingUtils.convertDrawableToDeviceDataModel(
                it,
                SendingData(
                    rootView.invertLED.isChecked,
                    rootView.flash.isChecked,
                    rootView.marquee.isChecked,
                    Mode.values()[modeAdapter.getSelectedItemPosition()],
                    Speed.values()[rootView.speedKnob.progress.minus(1)]
                )
            )
        } ?: DataToSend(listOf())
    }

    private fun setupButton() {
        rootView.save_button.setOnClickListener {
            rootView.text_to_send.hideKeyboard()
            showSaveFileDialog()
        }

        rootView.transfer_button.setOnClickListener {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                // Easter egg
                Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()

                rootView.transfer_button.visibility = View.GONE
                rootView.send_progress.visibility = View.VISIBLE

                val buttonTimer = Timer()
                buttonTimer.schedule(object : TimerTask() {
                    override fun run() {
                        activity?.runOnUiThread {
                            rootView.transfer_button.visibility = View.VISIBLE
                            rootView.send_progress.visibility = View.GONE
                        }
                    }
                }, SCAN_TIMEOUT_MS)

                SendingUtils.sendMessage(requireContext(), getSendData())
            } else {
                Toast.makeText(requireContext(), getString(R.string.enable_bluetooth), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupTextDrawableSection() {
        rootView.radioGroup.setOnCheckedChangeListener { _, optionId ->
            when (optionId) {
                R.id.drawableRadio -> {
                    rootView.text_to_send.hideKeyboard()

                    rootView.section_drawables.visibility = View.VISIBLE
                    rootView.section_text.visibility = View.GONE
                    selectDrawable(drawableRecyclerAdapter.getSelectedItem())
                }

                R.id.textRadio -> {
                    rootView.text_to_send.requestFocus()
                    rootView.text_to_send.showKeyboard()

                    rootView.section_text.visibility = View.VISIBLE
                    rootView.section_drawables.visibility = View.GONE
                    selectText()
                }
            }
            mainViewModel.radioSelectedId = optionId
        }

        rootView.text_to_send.setText(mainViewModel.text)
        rootView.text_to_send.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.text = s.toString()
                selectText()
            }
        })

        if (mainViewModel.radioSelectedId == R.id.drawableRadio) rootView.radioGroup.check(R.id.drawableRadio)
        else rootView.radioGroup.check(R.id.textRadio)
    }

    private fun setupTabLayout() {
        val speedTab = rootView.tabLayout.newTab().setText(getString(R.string.speed))
        val modeTab = rootView.tabLayout.newTab().setText(getString(R.string.mode))
        val effectsTab = rootView.tabLayout.newTab().setText(getString(R.string.effects))

        rootView.tabLayout.addTab(speedTab)
        rootView.tabLayout.addTab(modeTab)
        rootView.tabLayout.addTab(effectsTab)
        rootView.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getString(R.string.speed) -> {
                        rootView.speedLayout.visibility = View.VISIBLE
                        rootView.modeRecyclerView.visibility = View.GONE
                        rootView.effectsLayout.visibility = View.GONE
                        mainViewModel.currentTab = 1
                    }
                    getString(R.string.mode) -> {
                        rootView.speedLayout.visibility = View.GONE
                        rootView.modeRecyclerView.visibility = View.VISIBLE
                        rootView.effectsLayout.visibility = View.GONE
                        mainViewModel.currentTab = 2
                    }
                    getString(R.string.effects) -> {
                        rootView.speedLayout.visibility = View.GONE
                        rootView.modeRecyclerView.visibility = View.GONE
                        rootView.effectsLayout.visibility = View.VISIBLE
                        mainViewModel.currentTab = 3
                    }
                }
            }
        })
        rootView.tabLayout.selectTab(when (mainViewModel.currentTab) {
            1 -> speedTab
            2 -> modeTab
            else -> effectsTab
        }, true)
    }

    private fun setupSpeedKnob() {
        rootView.speedKnob.progress = mainViewModel.speed
        rootView.speedKnob.setOnProgressChangedListener(object : Croller.OnProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                rootView.textSpeed.text = progress.toString()
                mainViewModel.speed = progress
                setPreview()
            }
        })
    }

    private fun configureEffects() {
        rootView.card_effect_flash.setOnClickListener {
            rootView.flash.isChecked = !rootView.flash.isChecked
            mainViewModel.isFlash = rootView.flash.isChecked
            setBackgroundOf(rootView.card_effect_flash, rootView.effect_flash, rootView.flash_title, rootView.flash.isChecked)
            setPreview()
        }
        rootView.card_effect_marquee.setOnClickListener {
            rootView.marquee.isChecked = !rootView.marquee.isChecked
            mainViewModel.isMarquee = rootView.marquee.isChecked
            setBackgroundOf(rootView.card_effect_marquee, rootView.effect_marquee, rootView.marquee_title, rootView.marquee.isChecked)
            setPreview()
        }
        rootView.card_effect_invertLED.setOnClickListener {
            rootView.invertLED.isChecked = !rootView.invertLED.isChecked
            mainViewModel.isInverted = rootView.invertLED.isChecked
            setBackgroundOf(rootView.card_effect_invertLED, rootView.effect_invertLED, rootView.invertLED_title, rootView.invertLED.isChecked)
            setPreview()
        }
        setBackgroundOf(rootView.card_effect_flash, rootView.effect_flash, rootView.flash_title, mainViewModel.isFlash)
        setBackgroundOf(rootView.card_effect_marquee, rootView.effect_marquee, rootView.marquee_title, mainViewModel.isMarquee)
        setBackgroundOf(rootView.card_effect_invertLED, rootView.effect_invertLED, rootView.invertLED_title, mainViewModel.isInverted)
        rootView.marquee.isChecked = mainViewModel.isMarquee
        rootView.flash.isChecked = mainViewModel.isFlash
        rootView.invertLED.isChecked = mainViewModel.isInverted
    }

    private fun setBackgroundOf(card: LinearLayout?, image: GifImageView?, title: TextView?, checked: Boolean) {
        card?.background = if (checked) context?.resources?.getDrawable(R.color.colorAccent) else context?.resources?.getDrawable(android.R.color.transparent)
        image?.setColorFilter((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
        title?.setTextColor((if (checked) context?.resources?.getColor(android.R.color.white)
        else context?.resources?.getColor(android.R.color.black)) ?: Color.parseColor("#000000"))
    }

    private fun setupRecyclerViews() {
        rootView.drawablesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rootView.drawablesRecyclerView.adapter = null

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

        val drawableListener = object : OnDrawableSelected {
            override fun onSelected(selectedItemPosition: Int) {
                mainViewModel.drawablePosition = selectedItemPosition
                drawableRecyclerAdapter.setSelectedDrawablePosition(selectedItemPosition)
                drawableRecyclerAdapter.notifyDataSetChanged()
                selectDrawable(drawableRecyclerAdapter.getSelectedItem())
            }
        }
        drawableRecyclerAdapter.apply {
            onDrawableSelected = drawableListener
        }
        drawableRecyclerAdapter.addAll(listOfDrawables)
        drawableRecyclerAdapter
                .setSelectedDrawablePosition(mainViewModel.drawablePosition)
        rootView.drawablesRecyclerView.adapter = drawableRecyclerAdapter

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val gridManager = GridLayoutManager(context, 20)
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((position + 1) > 5) 5 else 4
                }
            }
            rootView.modeRecyclerView.layoutManager = gridManager
        } else {
            rootView.modeRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        rootView.modeRecyclerView.adapter = null

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
        modeAdapter.setSelectedAnimationPosition(mainViewModel.animationPosition)
        val modeListener = object : OnModeSelected {
            override fun onSelected(position: Int) {
                mainViewModel.animationPosition = position
                modeAdapter.setSelectedAnimationPosition(position)
                modeAdapter.notifyDataSetChanged()
                setPreview()
            }
        }
        modeAdapter.apply {
            onModeSelected = modeListener
        }
        rootView.modeRecyclerView.adapter = modeAdapter
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.radioSelectedId == R.id.textRadio) {
            rootView.text_to_send.requestFocus()
            rootView.text_to_send.showKeyboard()
        }
    }

    override fun onPause() {
        super.onPause()

        if (mainViewModel.radioSelectedId == R.id.textRadio) {
            rootView.text_to_send.hideKeyboard()
        }
    }

    private fun setPreview() {
        if (rootView.textRadio.isChecked)
            selectText()
        else
            selectDrawable(drawableRecyclerAdapter.getSelectedItem())
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

    private fun configToJSON(): String {
        return SendingUtils.configToJSON(
            mainViewModel.radioSelectedId,
            rootView.text_to_send.text.toString(),
            drawableRecyclerAdapter.getSelectedItem(),
            SendingData(
                rootView.invertLED.isChecked,
                rootView.flash.isChecked,
                rootView.marquee.isChecked,
                Mode.values()[modeAdapter.getSelectedItemPosition()],
                Speed.values()[rootView.speedKnob.progress.minus(1)]
            )
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
        viewModel?.let { StoreAsync(fileName, jsonString, it).execute() }
    }

    class StoreAsync(private val filename: String, private val json: String, private val viewModel: AppViewModel) : AsyncTask<Void, Void, Void>() {
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