package org.fossasia.badgemagic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentDrawBinding
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.StorageUtils
import org.koin.android.ext.android.inject

class DrawFragment : BaseFragment() {

    private lateinit var binding: FragmentDrawBinding

    companion object {
        @JvmStatic
        fun newInstance() =
            DrawFragment()
    }

    private val storageUtils: StorageUtils by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDrawBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel?.savedButton?.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    if (storageUtils.saveClipArt(Converters.convertStringsToLEDHex(binding.drawLayout.getCheckedList()))) {
                        Toast.makeText(requireContext(), R.string.clipart_saved_success, Toast.LENGTH_LONG).show()
                        binding.viewModel?.updateCliparts()
                    } else
                        Toast.makeText(requireContext(), R.string.clipart_saved_error, Toast.LENGTH_LONG).show()
                }
            }
        )

        binding.viewModel?.resetButton?.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    binding.drawLayout.resetCheckListWithDummyData()
                }
            }
        )
    }
}
