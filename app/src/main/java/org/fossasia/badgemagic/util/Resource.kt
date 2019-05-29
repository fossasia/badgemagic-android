package org.fossasia.badgemagic.util

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.fossasia.badgemagic.ui.App

class Resource {
    private val context by lazy {
        App.appContext
    }

    fun getResources() = context?.resources
    fun getDrawable(@DrawableRes resId: Int) = context?.getDrawable(resId)
    fun getString(@StringRes resId: Int) = context?.getString(resId)
    fun getColor(@ColorRes resId: Int) = context?.resources?.getColor(resId)
}