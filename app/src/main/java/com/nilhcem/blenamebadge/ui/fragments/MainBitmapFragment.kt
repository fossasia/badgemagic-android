package com.nilhcem.blenamebadge.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.ui.fragments.interfaces.PreviewChangeListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainBitmapFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: PreviewChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_text, container, false)
    }

    fun onButtonPressed() {
        listener?.onPreviewChange()
    }

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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MainBitmapFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
