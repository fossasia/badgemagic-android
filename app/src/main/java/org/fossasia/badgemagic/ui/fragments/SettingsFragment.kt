package org.fossasia.badgemagic.ui.fragments

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentSettingsBinding
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.viewmodels.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater, R.layout.fragment_settings, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changedLanguage.observe(viewLifecycleOwner, Observer {

            Snackbar
                .make(view, requireContext().getString(R.string.change_language), Snackbar.LENGTH_INDEFINITE)
                .setAction("RESTART") {
                    requireActivity().finishAffinity()
                    startActivity(requireActivity().intent)
                }
                .show()
        })
    }
}