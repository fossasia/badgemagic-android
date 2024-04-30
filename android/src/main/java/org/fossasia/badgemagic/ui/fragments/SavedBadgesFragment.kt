package org.fossasia.badgemagic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.adapter.OnSavedItemSelected
import org.fossasia.badgemagic.adapter.SaveAdapter
import org.fossasia.badgemagic.data.ConfigInfo
import org.fossasia.badgemagic.data.DataToSend
import org.fossasia.badgemagic.data.Mode
import org.fossasia.badgemagic.data.Speed
import org.fossasia.badgemagic.databinding.FragmentMainSaveBinding
import org.fossasia.badgemagic.ui.EditBadgeActivity
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.BluetoothAdapter
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.viewmodels.FilesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class SavedBadgesFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SavedBadgesFragment()
    }

    private var recyclerAdapter: SaveAdapter? = null

    private val viewModel by sharedViewModel<FilesViewModel>()

    private val bluetoothAdapter: BluetoothAdapter by inject()

    private var _binding: FragmentMainSaveBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        updateEmptyLayout()
    }

    private fun updateEmptyLayout() = with(binding) {
        if (viewModel.getFiles().value.isNullOrEmpty()) {
            savedText.visibility = View.GONE
            emptySavedLayout.visibility = View.VISIBLE
            previewBadge.visibility = View.GONE
        } else {
            savedText.visibility = View.VISIBLE
            emptySavedLayout.visibility = View.GONE
            previewBadge.visibility = View.VISIBLE
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

    private fun setupRecycler() = with(binding) {
        if (savedConfigRecyclerView == null) return
        savedConfigRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getFiles().observe(
            this@SavedBadgesFragment,
            Observer { files ->
                recyclerAdapter = null
                savedConfigRecyclerView.adapter = null

                recyclerAdapter = SaveAdapter(
                    requireContext(), files,
                    object : OnSavedItemSelected {
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

                        override fun onOptionSelectDelete(item: ConfigInfo) {
                            deleteWarning(item)
                        }

                        override fun transfer(item: ConfigInfo) {
                            transferItem(item)
                        }

                        override fun export(item: ConfigInfo) {
                            if (bluetoothAdapter.isTurnedOn(requireContext())) {
                                Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()
                                SendingUtils.sendMessage(requireContext(), getSendData())
                            }
                        }

                        override fun onSelected(item: ConfigInfo?) {
                            if (item != null)
                                setPreview(item.badgeJSON)
                            else
                                setPreviewNull()
                        }
                    }
                )
                savedConfigRecyclerView.adapter = recyclerAdapter
                updateEmptyLayout()
            }
        )
    }

    private fun transferItem(item: ConfigInfo) {
        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.type = "text/*"
        intentShareFile.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(
                requireContext(),
                getString(R.string.file_provider_authority),
                File(
                    viewModel.getAbsPath(item.fileName)
                )
            )
        )
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Badge Magic Share: " + item.fileName)
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Badge Magic Share: " + item.fileName)

        this.startActivity(Intent.createChooser(intentShareFile, item.fileName))
    }

    private fun deleteWarning(item: ConfigInfo) {
        val dialogMessage = getString(R.string.badge_delete_warning)
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setIcon(resources.getDrawable(R.drawable.ic_delete_black_24dp))
        builder.setTitle(getString(R.string.delete))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            viewModel.deleteFile(item.fileName)
            setPreviewNull()
            Toast.makeText(context, getString(R.string.delete_badge_confirm), Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
        }
        builder.create().show()
    }

    private fun setPreviewNull() {
        binding.previewBadge.setValue(
            Converters.convertTextToLEDHex(
                " ",
                false
            ).second,
            ifMar = false,
            ifFla = false,
            speed = Speed.ONE,
            mode = Mode.LEFT
        )
    }

    private fun setPreview(badgeJSON: String) {
        val badgeConfig = SendingUtils.getBadgeFromJSON(badgeJSON)

        binding.previewBadge.setValue(
            Converters.fixLEDHex(
                badgeConfig.hexStrings, badgeConfig.isInverted
            ),
            badgeConfig.isMarquee,
            badgeConfig.isFlash,
            badgeConfig.speed,
            badgeConfig.mode
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
