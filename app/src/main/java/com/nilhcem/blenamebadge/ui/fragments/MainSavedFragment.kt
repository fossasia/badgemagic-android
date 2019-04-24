package com.nilhcem.blenamebadge.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.adapter.OnSavedItemSelected
import com.nilhcem.blenamebadge.adapter.SaveAdapter
import com.nilhcem.blenamebadge.data.ConfigInfo
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.device.model.Message
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed
import com.nilhcem.blenamebadge.util.Converters
import com.nilhcem.blenamebadge.util.MoshiUtils
import com.nilhcem.blenamebadge.util.StorageUtils
import kotlinx.android.synthetic.main.fragment_main_save.recycler_view
import java.io.File

class MainSavedFragment : BaseFragment() {

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

    override fun updateSavedList() {
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
        if (selectedItem != null) {
            val badgeConfig = MoshiUtils.getAdapter().fromJson(selectedItem.badgeJSON)
            return DataToSend(listOf(Message(
                Converters.fixLEDHex(badgeConfig?.hexStrings as List<String>, badgeConfig.isInverted),
                badgeConfig.isMarquee,
                badgeConfig.isFlash,
                badgeConfig.speed,
                badgeConfig.mode
            )))
        } else {
            return DataToSend(listOf(Message(
                Converters.convertTextToLEDHex(
                    " ",
                    false
                ).second,
                false,
                false,
                Speed.ONE,
                Mode.LEFT
            )))
        }
    }

    private fun setupRecycler() {
        recycler_view.adapter = null
        recycler_view.layoutManager = LinearLayoutManager(context)

        val listOfDrawables = StorageUtils.getAllFiles()

        recyclerAdapter = SaveAdapter(context, listOfDrawables, object : OnSavedItemSelected {
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
        recycler_view.adapter = recyclerAdapter
    }

    private fun showLoadAlert(item: ConfigInfo) {
        val alertDialog = AlertDialog.Builder(context as Context)
            .setTitle(getString(R.string.saveactivity_operation_title))
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.share, null)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                StorageUtils.deleteFile(item.fileName)
                setupRecycler()
                alertDialog.dismiss()
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {

                val intentShareFile = Intent(Intent.ACTION_SEND)
                intentShareFile.type = "text/*"
                intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    context as Context,
                    getString(R.string.file_provider_authority),
                    File(StorageUtils.getAbsolutePathofFiles(item.fileName))))
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Badge Magic Share: " + item.fileName)
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Badge Magic Share: " + item.fileName)

                this.startActivity(Intent.createChooser(intentShareFile, item.fileName))
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private fun setPreviewNull() {
        listener?.onPreviewChange(
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
        val badgeConfig = MoshiUtils.getAdapter().fromJson(badgeJSON)
        listener?.onPreviewChange(
            Converters.fixLEDHex(badgeConfig?.hexStrings as List<String>, badgeConfig.isInverted),
            badgeConfig.isMarquee,
            badgeConfig.isFlash,
            badgeConfig.speed,
            badgeConfig.mode
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_save, container, false)
    }
}