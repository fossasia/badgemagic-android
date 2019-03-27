package com.nilhcem.blenamebadge.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {

    fun hideSoftKeyboard(context: Context?, view: View) {
        val inputManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED)
    }
}