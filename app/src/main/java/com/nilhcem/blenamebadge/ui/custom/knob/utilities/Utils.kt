package com.nilhcem.blenamebadge.ui.custom.knob.utilities

import android.content.Context
import android.util.DisplayMetrics

object Utils {
    fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toDouble()).toFloat()
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
