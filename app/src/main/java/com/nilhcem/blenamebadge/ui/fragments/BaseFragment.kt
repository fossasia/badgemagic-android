package com.nilhcem.blenamebadge.ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.nilhcem.blenamebadge.device.model.DataToSend
import com.nilhcem.blenamebadge.ui.interfaces.PreviewChangeListener

open class BaseFragment : Fragment(), BaseFragmentInterface {

    override fun initializePreview() {
    }

    override fun updateSavedList() {
    }

    override fun getSendData(): DataToSend {
        return DataToSend(listOf())
    }

    internal var listener: PreviewChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PreviewChangeListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

interface BaseFragmentInterface {
    fun getSendData(): DataToSend
    fun updateSavedList()
    fun initializePreview()
}