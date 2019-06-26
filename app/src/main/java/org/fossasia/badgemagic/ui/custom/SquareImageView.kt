package org.fossasia.badgemagic.ui.custom

import androidx.appcompat.widget.AppCompatImageView
import android.content.Context
import android.util.AttributeSet
import kotlin.math.min

class SquareImageView : AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = min(measuredWidth, measuredHeight)
        setMeasuredDimension(width, width)
    }
}
