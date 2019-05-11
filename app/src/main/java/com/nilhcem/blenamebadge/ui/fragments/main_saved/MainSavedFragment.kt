package com.nilhcem.blenamebadge.ui.fragments.main_saved

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

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.OnSavedItemSelected
import com.nilhcem.blenamebadge.adapter.SaveAdapter
import com.nilhcem.blenamebadge.data.fragments.ConfigInfo
import com.nilhcem.blenamebadge.data.device.model.DataToSend
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.ui.AppViewModel
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment
import com.nilhcem.blenamebadge.util.Converters
import com.nilhcem.blenamebadge.util.InjectorUtils
import com.nilhcem.blenamebadge.util.SendingUtils
import kotlinx.android.synthetic.main.fragment_main_save.view.*
import java.io.File

class MainSavedFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSavedFragment()
    }

    private var recyclerAdapter: SaveAdapter? = null
    private lateinit var viewModel: AppViewModel
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun inject() {
        viewModel = ViewModelProviders.of(this, InjectorUtils.provideFilesViewModelFactory())
            .get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_save, container, false)

        setupRecycler()

        return rootView
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
        if (rootView.savedConfigRecyclerView == null) return
        rootView.savedConfigRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.getFiles().observe(this, Observer { files ->
            recyclerAdapter = null
            rootView.savedConfigRecyclerView.adapter = null

            recyclerAdapter = SaveAdapter(context, files, object : OnSavedItemSelected {
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
            rootView.savedConfigRecyclerView.adapter = recyclerAdapter
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
        rootView.preview_badge.setValue(
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

        rootView.preview_badge.setValue(
            Converters.fixLEDHex(
                badgeConfig?.hexStrings ?: listOf(), badgeConfig?.isInverted ?: false),
            badgeConfig?.isMarquee ?: false,
            badgeConfig?.isFlash ?: false,
            badgeConfig?.speed ?: Speed.ONE,
            badgeConfig?.mode ?: Mode.LEFT
        )
    }
}