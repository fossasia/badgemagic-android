package org.fossasia.badgemagic.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.AsyncTask
import android.os.Bundle
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
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.adapter.DrawableAdapter
import org.fossasia.badgemagic.adapter.ModeAdapter
import org.fossasia.badgemagic.adapter.OnDrawableSelected
import org.fossasia.badgemagic.adapter.OnModeSelected
import org.fossasia.badgemagic.core.android.ext.hideKeyboard
import org.fossasia.badgemagic.core.android.ext.showKeyboard
import org.fossasia.badgemagic.data.DataToSend
import org.fossasia.badgemagic.data.DrawableInfo
import org.fossasia.badgemagic.data.Message
import org.fossasia.badgemagic.data.Mode
import org.fossasia.badgemagic.data.ModeInfo
import org.fossasia.badgemagic.data.Speed
import org.fossasia.badgemagic.databinding.FragmentMainTextartBinding
import org.fossasia.badgemagic.text.CenteredImageSpan
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.ui.custom.knob.Croller
import org.fossasia.badgemagic.util.BluetoothAdapter
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.DRAWABLE_END
import org.fossasia.badgemagic.util.DRAWABLE_START
import org.fossasia.badgemagic.util.ImageUtils
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.viewmodels.TextArtViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

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

    private val bluetoothAdapter: BluetoothAdapter by inject()

    private var _binding: FragmentMainTextartBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentMainTextartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getSendData(): DataToSend = with(binding) {
        textViewMainText.hideKeyboard()
        return SendingUtils.convertToDeviceDataModel(
            Message(
                Converters.convertEditableToLEDHex(
                    textViewMainText.text.toString(),
                    sectionsTab?.effects?.invertLED!!.isChecked,
                    viewModel.getClipArts().value ?: SparseArray()
                ),
                sectionsTab.effects.flash?.isChecked!!,
                sectionsTab.effects.marquee?.isChecked!!,
                Speed.values()[sectionsTab.speedKnob?.progress?.minus(1)!!],
                Mode.values()[modeAdapter.getSelectedItemPosition()]
            )
        )
    }

    override fun initializePreview() {
        setPreview()
    }

    private fun setupButton() = with(binding) {
        saveButton.setOnClickListener {
            if (checkStoragePermission()) {
                startSaveFile()
            }
        }

        transferButton.setOnClickListener {
            if (textViewMainText.text.trim().toString() != "") {
                if (bluetoothAdapter.isTurnedOn(requireContext())) {
                    // Easter egg
                    Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()

                    transferButton.visibility = View.GONE
                    sendProgress.visibility = View.VISIBLE

                    val buttonTimer = Timer()
                    buttonTimer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                activity?.runOnUiThread {
                                    transferButton.visibility = View.VISIBLE
                                    sendProgress.visibility = View.GONE
                                }
                            }
                        },
                        SCAN_TIMEOUT_MS
                    )

                    SendingUtils.sendMessage(requireContext(), getSendData())
                }
            } else
                Toast.makeText(requireContext(), getString(R.string.empty_text_to_send), Toast.LENGTH_LONG).show()
        }
    }

    private fun startSaveFile() {
        binding.textViewMainText.hideKeyboard()
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
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            false
        } else
            true
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

    private fun setupTextArtSection() = with(binding) {
        textViewMainText.requestFocus()
        textViewMainText.showKeyboard()

        textViewMainText.setText(viewModel.text)

        clipartHandlerLayout.setOnClickListener {
            viewModel.showClipart = !viewModel.showClipart
            toggleEmojiSection()
        }
        toggleEmojiSection()
    }

    private fun toggleEmojiSection() = with(binding) {
        viewModel.showClipart.let {
            clipartLayout.visibility = if (it) View.VISIBLE else View.GONE
            clipartHandler.setImageResource(
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
            binding.sectionsTab?.run {
                when (tab?.text) {
                    getString(R.string.speed) -> {
                        speedLayout.visibility = View.VISIBLE
                        modeRecyclerView.visibility = View.GONE
                        effects.effectsLayout?.visibility = View.GONE
                        viewModel.currentTab = 1
                    }

                    getString(R.string.mode) -> {
                        speedLayout.visibility = View.GONE
                        modeRecyclerView.visibility = View.VISIBLE
                        effects.effectsLayout?.visibility = View.GONE
                        viewModel.currentTab = 2
                    }

                    getString(R.string.effects) -> {
                        speedLayout.visibility = View.GONE
                        modeRecyclerView.visibility = View.GONE
                        effects.effectsLayout?.visibility = View.VISIBLE
                        viewModel.currentTab = 3
                    }
                }
            }
        }
    }

    private fun setupTabLayout() = binding.sectionsTab?.run {
        val speedTab = tabLayout.newTab().setText(getString(R.string.speed))
        val modeTab = tabLayout.newTab().setText(getString(R.string.mode))
        val effectsTab = tabLayout.newTab().setText(getString(R.string.effects))

        tabLayout.addTab(speedTab)
        tabLayout.addTab(modeTab)
        tabLayout.addTab(effectsTab)
        tabLayout.selectTab(
            when (viewModel.currentTab) {
                1 -> speedTab
                2 -> modeTab
                else -> effectsTab
            },
            true
        )
    }

    override fun onStart() {
        super.onStart()
        binding.textViewMainText.addTextChangedListener(textChangedListener)
        binding.sectionsTab?.tabLayout?.addOnTabSelectedListener(tabSelectedListener)
    }

    override fun onStop() {
        super.onStop()
        binding.textViewMainText.removeTextChangedListener(textChangedListener)
        binding.sectionsTab?.tabLayout?.removeOnTabSelectedListener(tabSelectedListener)
    }

    private fun setupSpeedKnob() = binding.sectionsTab?.run {
        speedKnob.progress = viewModel.speed
        speedKnob.setOnProgressChangedListener(object : Croller.OnProgressChangedListener {
            override fun onProgressChanged(progress: Int) {
                textSpeed.text = progress.toString()
                viewModel.speed = progress
                setPreview()
            }
        })
    }

    private fun configureEffects() = binding.sectionsTab?.effects?.run {
        cardEffectFlash.setOnClickListener {
            flash.isChecked = !flash.isChecked
            viewModel.isFlash = flash.isChecked
            setBackgroundOf(cardEffectFlash, effectFlash, flashTitle, flash.isChecked)
            setPreview()
        }
        cardEffectMarquee.setOnClickListener {
            marquee.isChecked = !marquee.isChecked
            viewModel.isMarquee = marquee.isChecked
            setBackgroundOf(cardEffectMarquee, effectMarquee, marqueeTitle, marquee.isChecked)
            setPreview()
        }
        cardEffectInvertLED.setOnClickListener {
            invertLED.isChecked = !invertLED.isChecked
            viewModel.isInverted = invertLED.isChecked
            setBackgroundOf(cardEffectInvertLED, effectInvertLED, invertLEDTitle, invertLED.isChecked)
            setPreview()
        }
        setBackgroundOf(cardEffectFlash, effectFlash, flashTitle, viewModel.isFlash)
        setBackgroundOf(cardEffectMarquee, effectMarquee, marqueeTitle, viewModel.isMarquee)
        setBackgroundOf(cardEffectInvertLED, effectInvertLED, invertLEDTitle, viewModel.isInverted)
        marquee.isChecked = viewModel.isMarquee
        flash.isChecked = viewModel.isFlash
        invertLED.isChecked = viewModel.isInverted
    }

    private fun setBackgroundOf(card: LinearLayout?, image: GifImageView?, title: TextView?, checked: Boolean) {
        card?.background = if (checked) context?.resources?.getDrawable(R.color.colorAccent) else context?.resources?.getDrawable(android.R.color.transparent)
        image?.setColorFilter(
            (
                if (checked) context?.resources?.getColor(android.R.color.white)
                else context?.resources?.getColor(android.R.color.black)
                ) ?: Color.parseColor("#000000")
        )
        title?.setTextColor(
            (
                if (checked) context?.resources?.getColor(android.R.color.white)
                else context?.resources?.getColor(android.R.color.black)
                ) ?: Color.parseColor("#000000")
        )
    }

    private fun setupRecyclerViews() = with(binding) {
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

        viewModel.getClipArts().observe(
            viewLifecycleOwner,
            Observer {
                val listOfDrawable = mutableListOf<DrawableInfo>()
                for (i in 0 until it.size()) {
                    val key = it.keyAt(i)
                    val obj = it.get(key)
                    listOfDrawable.add(DrawableInfo(key, obj))
                }

                drawableRecyclerAdapter.addAll(listOfDrawable)
            }
        )

        drawablesRecyclerView.adapter = drawableRecyclerAdapter

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val gridManager = GridLayoutManager(context, 20)
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((position + 1) > 5) 5 else 4
                }
            }
            sectionsTab?.modeRecyclerView?.layoutManager = gridManager
        } else {
            sectionsTab?.modeRecyclerView?.layoutManager = GridLayoutManager(context, 3)
        }

        sectionsTab?.modeRecyclerView?.adapter = null

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
        sectionsTab?.modeRecyclerView?.adapter = modeAdapter
    }

    private fun putBitmapInEditText(drawableInfo: DrawableInfo) {
        val strToAppend = "«${drawableInfo.id}»"
        val spanStringBuilder = SpannableStringBuilder(strToAppend)
        if (drawableInfo.image is VectorDrawable)
            spanStringBuilder.setSpan(CenteredImageSpan(requireContext(), ImageUtils.trim(ImageUtils.vectorToBitmap(drawableInfo.image), 70)), 0, strToAppend.length, 33)
        else if (drawableInfo.image is BitmapDrawable)
            spanStringBuilder.setSpan(CenteredImageSpan(requireContext(), ImageUtils.trim((drawableInfo.image).bitmap, 70)), 0, strToAppend.length, 33)

        val editable = binding.textViewMainText.text
        val n = binding.textViewMainText.selectionEnd
        if (n < editable.length) {
            editable.insert(n, spanStringBuilder)
            return
        }
        editable.append(spanStringBuilder)
    }

    override fun onResume() {
        super.onResume()
        binding.textViewMainText.requestFocus()
        binding.textViewMainText.showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        binding.textViewMainText.hideKeyboard()
    }

    private fun setPreview() = with(binding) {
        previewBadge.setValue(
            Converters.convertEditableToLEDHex(
                textViewMainText.text.toString(),
                sectionsTab?.effects?.invertLED!!.isChecked,
                viewModel.getClipArts().value ?: SparseArray()
            ),
            sectionsTab?.effects?.marquee!!.isChecked,
            sectionsTab?.effects?.flash!!.isChecked,
            Speed.values()[sectionsTab?.speedKnob?.progress?.minus(1)!!],
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

    private fun configToJSON(): String = with(binding) {
        return SendingUtils.configToJSON(
            Message(
                Converters.convertEditableToLEDHex(
                    textViewMainText.text.toString(),
                    false, viewModel.getClipArts().value ?: SparseArray()
                ),
                sectionsTab?.effects?.flash!!.isChecked,
                sectionsTab?.effects?.marquee!!.isChecked,
                Speed.values()[sectionsTab?.speedKnob?.progress?.minus(1)!!],
                Mode.values()[modeAdapter.getSelectedItemPosition()]
            ),
            sectionsTab?.effects?.invertLED!!.isChecked
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
