package com.nilhcem.blenamebadge.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.EditText
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.DrawableAdapter
import com.nilhcem.blenamebadge.adapter.OnDrawableSelected
import com.nilhcem.blenamebadge.core.android.ext.hideKeyboard
import com.nilhcem.blenamebadge.core.android.ext.showKeyboard
import com.nilhcem.blenamebadge.data.BadgeConfig
import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import com.nilhcem.blenamebadge.ui.interfaces.PreviewChangeListener
import com.nilhcem.blenamebadge.util.Converters
import com.nilhcem.blenamebadge.util.MoshiUtils
import com.nilhcem.blenamebadge.util.StorageUtils
import kotlinx.android.synthetic.main.fragment_main_text.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class MainTextDrawableFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            MainTextDrawableFragment()
    }

    private lateinit var drawableRecyclerAdapter: DrawableAdapter

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

    private fun selectText() {
        val (valid, textToSend) = Converters.convertTextToLEDHex(if (text_to_send.text.isNotEmpty()) text_to_send.text.toString() else if (!invertLED.isChecked) " " else "", invertLED.isChecked)
        if (!valid) {
            Toast.makeText(context, R.string.character_not_found, Toast.LENGTH_SHORT).show()
        }
        listener?.onPreviewChange(
            textToSend,
            marquee.isChecked,
            flash.isChecked,
            Speed.values()[speed.selectedItemPosition],
            Mode.values()[mode.selectedItemPosition]
        )
    }

    private fun selectDrawable(selectedItem: DrawableInfo?) {
        listener?.onPreviewChange(
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

    private fun convertTextToDeviceDataModel(): DataToSend {
        return DataToSend(listOf(Message(
            Converters.convertTextToLEDHex(
                if (text_to_send.text.isNotEmpty()) text_to_send.text.toString()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerItem = R.layout.spinner_item
        speed.adapter = ArrayAdapter<String>(context as Context, spinnerItem, Speed.values().mapIndexed { index, _ -> (index + 1).toString() })
        mode.adapter = ArrayAdapter<String>(context as Context, spinnerItem, Mode.values().map { getString(it.stringResId) })

        save_button.setOnClickListener {
            text_to_send.hideKeyboard()
            showSaveFileDialog()
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

        textRadio.isChecked = true

        setupRecycler()
    }

    private fun setupRecycler() {
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

    private fun removeListeners() {
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

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Calendar.getInstance().time)
    }

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
                    if (StorageUtils.checkIfFilePresent(fileTitle.toString())) {
                        showFileOverrideDialog(fileTitle.toString(), configToJSON(), listener as PreviewChangeListener)
                    } else {
                        saveFile(fileTitle.toString(), configToJSON(), listener as PreviewChangeListener)
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
        val bConfig = BadgeConfig()
        bConfig.hexStrings = when (selectedID) {
            R.id.textRadio -> {
                Converters.convertTextToLEDHex(if (text_to_send.text.isNotEmpty()) text_to_send.text.toString() else " ", false).second
            }
            else -> {
                if (drawableRecyclerAdapter.getSelectedItem() != null)
                    Converters.convertDrawableToLEDHex(drawableRecyclerAdapter.getSelectedItem()?.image
                        ?: resources.getDrawable(R.drawable.apple), invertLED.isChecked)
                else
                    Converters.convertTextToLEDHex(" ", false).second
            }
        }
        bConfig.isFlash = flash.isChecked
        bConfig.isMarquee = marquee.isChecked
        bConfig.isInverted = invertLED.isChecked
        bConfig.mode = Mode.values()[mode.selectedItemPosition]
        bConfig.speed = Speed.values()[speed.selectedItemPosition]

        return MoshiUtils.getAdapter().toJson(bConfig)
    }

    private fun showFileOverrideDialog(fileName: String, jsonString: String, listener: PreviewChangeListener) {
        AlertDialog.Builder(context)
                .setTitle(context?.getString(R.string.save_dialog_already_present))
                .setMessage(context?.getString(R.string.save_dialog_already_present_override))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                    saveFile(fileName, jsonString, listener)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }

    private fun saveFile(fileName: String, jsonString: String, listener: PreviewChangeListener) {
        StoreAsync(fileName, jsonString, listener).execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_text, container, false)
    }

    inner class StoreAsync(private val filename: String, private val json: String, private val listener: PreviewChangeListener) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            StorageUtils.saveFile(filename, json)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            listener.updateSavedList()
        }
    }
}