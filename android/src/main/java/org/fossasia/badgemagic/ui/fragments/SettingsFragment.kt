package org.fossasia.badgemagic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentSettingsBinding
import org.fossasia.badgemagic.ui.base.BaseFragment
import org.fossasia.badgemagic.util.PreferenceUtils
import org.fossasia.badgemagic.viewmodels.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }

    private val viewModel by viewModel<SettingsViewModel>()
    private val prefsUtils: PreferenceUtils by inject()

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

        viewModel.changedBadge.observe(viewLifecycleOwner, Observer {

            Snackbar
                .make(view, requireContext().getString(R.string.changed_badge) + " ${prefsUtils.selectedBadge}", Snackbar.LENGTH_LONG)
                .show()
        })
    }
}
