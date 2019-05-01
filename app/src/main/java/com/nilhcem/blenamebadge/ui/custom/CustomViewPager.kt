package com.nilhcem.blenamebadge.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var tempHeightMeasureSpec = heightMeasureSpec
        try {
            val currentPagePosition = 0
            val child = getChildAt(currentPagePosition)
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                tempHeightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec)
    }
}
