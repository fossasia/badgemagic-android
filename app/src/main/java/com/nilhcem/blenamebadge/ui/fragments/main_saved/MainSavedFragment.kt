package com.nilhcem.blenamebadge.ui.fragments.main_saved

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.OnSavedItemSelected
import com.nilhcem.blenamebadge.adapter.SaveAdapter
import com.nilhcem.blenamebadge.data.fragments.ConfigInfo
import com.nilhcem.blenamebadge.data.device.model.DataToSend
import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed
import com.nilhcem.blenamebadge.ui.fragments.base.BaseFragment
import com.nilhcem.blenamebadge.util.SendingUtils
import kotlinx.android.synthetic.main.fragment_main_save.savedConfigRecyclerView
import java.io.File

class MainSavedFragment : BaseFragment(), MainSavedNavigator {

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSavedFragment()
    }

    private var recyclerAdapter: SaveAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
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

    override fun setupRecycler() {
        if (savedConfigRecyclerView == null) return
        savedConfigRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel?.getFiles()?.observe(this, Observer { files ->
            recyclerAdapter = null
            savedConfigRecyclerView.adapter = null

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
            savedConfigRecyclerView.adapter = recyclerAdapter
        })
    }

    override fun showLoadAlert(item: ConfigInfo) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.saveactivity_operation_title))
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.share, null)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                viewModel?.deleteFile(item.fileName)

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
                        viewModel?.getAbsPath(item.fileName)
                    )))
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Badge Magic Share: " + item.fileName)
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Badge Magic Share: " + item.fileName)

                this.startActivity(Intent.createChooser(intentShareFile, item.fileName))
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    override fun setPreviewNull() {
        viewModel?.updatePreview(
            viewModel?.textToLEDHex(
                " ",
                false
            )?.second ?: listOf(),
            false,
            false,
            Speed.ONE,
            Mode.LEFT
        )
    }

    override fun setPreview(badgeJSON: String) {
        val badgeConfig = SendingUtils.getBadgeFromJSON(badgeJSON)
        viewModel?.updatePreview(
            viewModel?.fixLEDHex(badgeConfig?.hexStrings ?: listOf(), badgeConfig?.isInverted
                ?: false) ?: listOf(),
            badgeConfig?.isFlash ?: false,
            badgeConfig?.isMarquee ?: false,
            badgeConfig?.speed ?: Speed.ONE,
            badgeConfig?.mode ?: Mode.LEFT
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_save, container, false)
    }
}