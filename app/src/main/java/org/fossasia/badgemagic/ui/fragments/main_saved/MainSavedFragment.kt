package org.fossasia.badgemagic.ui.fragments.main_saved

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_save.*

import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.fragments.ConfigInfo
import org.fossasia.badgemagic.data.device.model.DataToSend
import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed
import org.fossasia.badgemagic.ui.AppViewModel
import org.fossasia.badgemagic.ui.fragments.base.BaseFragment
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.InjectorUtils
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.adapter.OnSavedItemSelected
import org.fossasia.badgemagic.adapter.SaveAdapter
import java.io.File

class MainSavedFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSavedFragment()
    }

    private var recyclerAdapter: SaveAdapter? = null
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun inject() {
        val currentActivity = activity
        if (currentActivity != null)
            viewModel = ViewModelProviders
                .of(currentActivity, InjectorUtils.provideFilesViewModelFactory())
                .get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        updateEmptyLayout()
    }

    private fun updateEmptyLayout() {
        if (viewModel.getFiles().value.isNullOrEmpty()) {
            saved_text.visibility = View.GONE
            empty_saved_layout.visibility = View.VISIBLE
        } else {
            saved_text.visibility = View.VISIBLE
            empty_saved_layout.visibility = View.GONE
        }
    }

    override fun initializePreview() {
        if (recyclerAdapter != null) {
            val selectedItem = recyclerAdapter?.getSelectedItem()
            if (selectedItem != null) {
                setPreview(selectedItem.badgeJSON)
            } else {
                setPreviewNull()
            }
        }
    }

    override fun getSendData(): DataToSend {
        val selectedItem = recyclerAdapter?.getSelectedItem()
        return if (selectedItem != null) {
            SendingUtils.returnMessageWithJSON(selectedItem.badgeJSON)
        } else {
            SendingUtils.returnDefaultMessage()
        }
    }

    private fun setupRecycler() {
        if (savedConfigRecyclerView == null) return
        savedConfigRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getFiles().observe(this, Observer { files ->
            recyclerAdapter = null
            savedConfigRecyclerView.adapter = null

            recyclerAdapter = SaveAdapter(requireContext(), files, object : OnSavedItemSelected {
                override fun onOptionsSelected(item: ConfigInfo) {
                    showLoadAlert(item)
                }

                override fun onSelected(item: ConfigInfo?) {
                    if (item != null)
                        setPreview(item.badgeJSON)
                    else
                        setPreviewNull()
                }
            })
            savedConfigRecyclerView.adapter = recyclerAdapter
            updateEmptyLayout()
        })
    }

    private fun showLoadAlert(item: ConfigInfo) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.saveactivity_operation_title))
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.share, null)
            .setNeutralButton(R.string.transfer_button, null)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                viewModel.deleteFile(item.fileName)

                alertDialog.dismiss()
                setPreviewNull()
                Toast.makeText(context, R.string.deleted_saved, Toast.LENGTH_SHORT).show()
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {

                val intentShareFile = Intent(Intent.ACTION_SEND)
                intentShareFile.type = "text/*"
                intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    requireContext(),
                    getString(R.string.file_provider_authority),
                    File(
                        viewModel.getAbsPath(item.fileName)
                    )))
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Badge Magic Share: " + item.fileName)
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Badge Magic Share: " + item.fileName)

                this.startActivity(Intent.createChooser(intentShareFile, item.fileName))
                alertDialog.dismiss()
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    // Easter egg
                    Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()
                    SendingUtils.sendMessage(requireContext(), getSendData())
                } else {
                    Toast.makeText(requireContext(), getString(R.string.enable_bluetooth), Toast.LENGTH_LONG).show()
                }
            }
        }
        alertDialog.show()
    }

    private fun setPreviewNull() {
        preview_badge.setValue(
            Converters.convertTextToLEDHex(
                " ",
                false
            ).second,
            false,
            false,
            Speed.ONE,
            Mode.LEFT
        )
    }

    private fun setPreview(badgeJSON: String) {
        val badgeConfig = SendingUtils.getBadgeFromJSON(badgeJSON)

        preview_badge.setValue(
            Converters.fixLEDHex(
                badgeConfig?.hexStrings ?: listOf(), badgeConfig?.isInverted ?: false),
            badgeConfig?.isMarquee ?: false,
            badgeConfig?.isFlash ?: false,
            badgeConfig?.speed ?: Speed.ONE,
            badgeConfig?.mode ?: Mode.LEFT
        )
    }
}