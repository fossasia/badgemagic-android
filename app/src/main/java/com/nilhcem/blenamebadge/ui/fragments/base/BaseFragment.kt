package com.nilhcem.blenamebadge.ui.fragments.base

import androidx.fragment.app.Fragment
import com.nilhcem.blenamebadge.data.device.model.DataToSend

open class BaseFragment : Fragment(), BaseFragmentInterface {
    override fun inject() {
    }

    override fun initializePreview() {
    }

    override fun getSendData(): DataToSend {
        return DataToSend(listOf())
    }
}

interface BaseFragmentInterface {
    fun getSendData(): DataToSend
    fun initializePreview()
    fun inject()
}