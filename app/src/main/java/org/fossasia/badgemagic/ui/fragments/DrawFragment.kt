package org.fossasia.badgemagic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_draw.*
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentDrawBinding
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.StorageUtils
import org.fossasia.badgemagic.viewmodels.DrawViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            DrawFragment()
    }

    private val drawViewModel by viewModel<DrawViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentDrawBinding>(inflater, R.layout.fragment_draw, container, false)
        binding.viewModel = drawViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawViewModel.savedButton.observe(this, Observer {
            if (it) {
                if (StorageUtils.saveClipArt(Converters.convertStringsToLEDHex(draw_layout.getCheckedList()))) {
                    Toast.makeText(requireContext(), R.string.clipart_saved_success, Toast.LENGTH_LONG).show()
                    drawViewModel.updateCliparts()
                } else
                    Toast.makeText(requireContext(), R.string.clipart_saved_error, Toast.LENGTH_LONG).show()
            }
        })

        drawViewModel.resetButton.observe(this, Observer {
            if (it) {
                draw_layout.resetCheckListWithDummyData()
            }
        })
    }
}