package org.fossasia.badgemagic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentSavedClipartsBinding
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.viewmodels.SavedClipartViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedClipartFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SavedClipartFragment()
    }

    private val viewModel by viewModel<SavedClipartViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSavedClipartsBinding>(inflater, R.layout.fragment_saved_cliparts, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
}