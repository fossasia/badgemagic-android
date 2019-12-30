package org.fossasia.badgemagic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.File
import kotlinx.android.synthetic.main.fragment_main_save.*
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.adapter.OnSavedItemSelected
import org.fossasia.badgemagic.adapter.SaveAdapter
import org.fossasia.badgemagic.data.device.model.DataToSend
import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed
import org.fossasia.badgemagic.data.fragments.ConfigInfo
import org.fossasia.badgemagic.ui.EditBadgeActivity
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.BluetoothManager
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.viewmodels.FilesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SavedBadgesFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SavedBadgesFragment()
    }

    private var recyclerAdapter: SaveAdapter? = null

    private val viewModel by sharedViewModel<FilesViewModel>()

    private val bluetoothManager: BluetoothManager by inject()

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
                override fun onEdit(item: ConfigInfo?) {
                    startActivity(
                        Intent(requireContext(), EditBadgeActivity::class.java).apply {
                            putExtra("badgeJSON", item?.badgeJSON)
                            putExtra("fileName", item?.fileName)
                        }
                    )
                    setPreviewNull()
                    recyclerAdapter?.resetSelectedItem()
                }

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
        val buttons = arrayOf(
            getString(R.string.share),
            getString(R.string.transfer_button),
            getString(R.string.delete)
        )
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.saveactivity_operation_title))
            .setItems(
                buttons
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        // Share Condition
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
                        dialog.dismiss()
                    }
                    1 -> {
                        // Transfer Condition
                        if (bluetoothManager.btAdapter.isEnabled) {
                            Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()
                            SendingUtils.sendMessage(requireContext(), getSendData())
                        } else {
                            showAlertDialog()
                        }
                    }
                    2 -> {
                        // Delete Condition
                        viewModel.deleteFile(item.fileName)
                        dialog.dismiss()
                        setPreviewNull()
                        Toast.makeText(requireContext(), R.string.deleted_saved, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .create()

        alertDialog.show()
    }

    private fun showAlertDialog() {
        val dialogMessage = getString(R.string.enable_bluetooth)
        val builder = android.app.AlertDialog.Builder(requireContext())
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
