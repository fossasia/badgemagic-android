package org.fossasia.badgemagic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_transfer.empty_transfer_layout
import kotlinx.android.synthetic.main.fragment_transfer.preview_badge
import kotlinx.android.synthetic.main.fragment_transfer.transferConfigRecyclerView
import kotlinx.android.synthetic.main.fragment_transfer.transfer_button
import kotlinx.android.synthetic.main.fragment_transfer.transfer_queue
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.adapter.OnTransferItemSelected
import org.fossasia.badgemagic.adapter.TransferAdapter
import org.fossasia.badgemagic.data.DataToSend
import org.fossasia.badgemagic.data.Message
import org.fossasia.badgemagic.data.Mode
import org.fossasia.badgemagic.data.Speed
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.BluetoothAdapter
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.viewmodels.TransferQueueViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TransferFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            TransferFragment()
    }

    private var recyclerAdapter: TransferAdapter? = null

    private val viewModel by sharedViewModel<TransferQueueViewModel>()

    private val bluetoothAdapter: BluetoothAdapter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transfer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        updateEmptyLayout()

        transfer_button.setOnClickListener {
            if (bluetoothAdapter.isTurnedOn(requireContext())) {
                Toast.makeText(requireContext(), getString(R.string.sending_data), Toast.LENGTH_LONG).show()
                SendingUtils.sendMessage(requireContext(), getSendData())
            }
        }
    }

    private fun updateEmptyLayout() {
        if (viewModel.items.value.isNullOrEmpty()) {
            transfer_button.visibility = View.GONE
            transfer_queue.visibility = View.GONE
            empty_transfer_layout.visibility = View.VISIBLE
        } else {
            transfer_button.visibility = View.VISIBLE
            transfer_queue.visibility = View.VISIBLE
            empty_transfer_layout.visibility = View.GONE
        }
    }

    override fun initializePreview() {
        if (recyclerAdapter != null) {
            val selectedItem = recyclerAdapter?.getSelectedItem()
            if (selectedItem != null) {
                setPreview(SendingUtils.configToJSON(selectedItem, false))
            } else {
                setPreviewNull()
            }
        }
    }

    override fun getSendData(): DataToSend {
        val messages = ArrayList<Message>()

        val items = recyclerAdapter?.getItems()
        if (!items.isNullOrEmpty()) {
            items.forEach {
                messages.add(SendingUtils.returnMessageWithJSON(SendingUtils.configToJSON(it, false)))
            }
        }

        if (messages.isEmpty()) {
            messages.add(SendingUtils.returnDefaultMessage())
        }

        return SendingUtils.convertToDeviceDataModel(messages)
    }

    private fun setupRecycler() {
        if (transferConfigRecyclerView == null) return
        transferConfigRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.items.observe(this, Observer { items ->
            recyclerAdapter = null
            transferConfigRecyclerView.adapter = null

            recyclerAdapter = TransferAdapter(requireContext(), items, object : OnTransferItemSelected {
                override fun onDelete(item: Message) {
                    delete(item)
                }

                override fun onSelected(item: Message?) {
                    if (item != null)
                        setPreview(SendingUtils.configToJSON(item, false))
                    else
                        setPreviewNull()
                }
            })
            transferConfigRecyclerView.adapter = recyclerAdapter
            updateEmptyLayout()
        })
    }

    private fun delete(item: Message) {
        viewModel.remove(item)
        setPreviewNull()
        setupRecycler()
    }

    private fun setPreviewNull() {
        preview_badge.setValue(
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

        preview_badge.setValue(
            Converters.fixLEDHex(
                badgeConfig.hexStrings, badgeConfig.isInverted),
            badgeConfig.isMarquee,
            badgeConfig.isFlash,
            badgeConfig.speed,
            badgeConfig.mode
        )
    }
}
