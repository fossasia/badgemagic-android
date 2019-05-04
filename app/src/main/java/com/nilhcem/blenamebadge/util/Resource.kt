package com.nilhcem.blenamebadge.util

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nilhcem.blenamebadge.ui.App

class Resource {
    private val context by lazy {
        App.appContext
    }

    fun getResources() = context?.resources
    fun getDrawable(@DrawableRes resId: Int) = context?.resources?.getDrawable(resId)
    fun getString(@StringRes resId: Int) = context?.getString(resId)
    fun getColor(@ColorRes resId: Int) = context?.resources?.getColor(resId)
}