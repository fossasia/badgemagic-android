package org.fossasia.badgemagic.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class Resource(val context: Context) {
    fun getResources() = context.resources
    fun getDrawable(@DrawableRes resId: Int) = context.getDrawable(resId)
    fun getString(@StringRes resId: Int) = context.getString(resId)
    fun getColor(@ColorRes resId: Int) = context.resources?.getColor(resId)
}