package com.nilhcem.blenamebadge.ui.fragments.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nilhcem.blenamebadge.data.device.model.DataToSend
import com.nilhcem.blenamebadge.ui.AppViewModel
import com.nilhcem.blenamebadge.util.InjectorUtils

open class BaseFragment : Fragment(), BaseFragmentInterface {

    var viewModel: AppViewModel? = null

    override fun inject() {
        val savedConfigFactory = InjectorUtils.provideFilesViewModelFactory()
        viewModel = ViewModelProviders.of(this, savedConfigFactory)
            .get(AppViewModel::class.java)
    }

    override fun initializePreview() {
    }

    override fun getSendData(): DataToSend {
        return DataToSend(listOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
    }
}

interface BaseFragmentInterface {
    fun getSendData(): DataToSend
    fun initializePreview()
    fun inject()
}