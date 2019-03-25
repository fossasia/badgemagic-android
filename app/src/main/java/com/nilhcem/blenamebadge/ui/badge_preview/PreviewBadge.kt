package com.nilhcem.blenamebadge.ui.badge_preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.View
import com.nilhcem.blenamebadge.R

import java.math.BigInteger
import java.util.ArrayList

class PreviewBadge : View {
    private var mLed: Drawable? = null
    private var mLedLit: Drawable? = null

    private lateinit var bgBounds: RectF
    private var cells = ArrayList<Cell>()

    private var checkList: ArrayList<CheckList>? = null

    private fun resetCheckList() {
        checkList = ArrayList()
        for (i in 0..10) {
            checkList!!.add(CheckList())
        }
    }

    constructor(context: Context) : super(context) {
        mLed = context.resources.getDrawable(R.drawable.ic_led)
        mLedLit = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        mLed = context.resources.getDrawable(R.drawable.ic_led)
        mLedLit = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()

    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mLed = context.resources.getDrawable(R.drawable.ic_led)
        mLedLit = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()

    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset = 30
        bgBounds = RectF((left + offset).toFloat(), (top + offset).toFloat(), (right - offset).toFloat(), (bottom - offset).toFloat())

        val singleCell = (right - left - offset * 2 - 10) / 44

        cells = ArrayList()
        for (i in 0..10) {
            cells.add(Cell())
            for (j in 0..43) {
                cells[i].list.add(Rect(
                        left + offset + 25 + j * singleCell,
                        top + offset + 25 + i * singleCell,
                        left + offset + 25 + j * singleCell + singleCell,
                        top + offset + 25 + i * singleCell + singleCell
                ))
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // Paint Configuration
        val bgPaint = Paint()
        bgPaint.isAntiAlias = true
        bgPaint.color = Color.parseColor("#000000")

        // Draw Background
        canvas.drawRoundRect(bgBounds, 25f, 25f, bgPaint)

        // Draw Cells
        for (i in 0..10) {
            for (j in 0..43) {
                if (i < checkList!!.size && j < checkList!![i].list.size && checkList!![i].list[j]) {
                    mLedLit!!.bounds = cells[i].list[j]
                    mLedLit!!.draw(canvas)
                } else {
                    mLed!!.bounds = cells[i].list[j]
                    mLed!!.draw(canvas)
                }
            }
        }
        super.onDraw(canvas)
    }

    fun setValue(allHex: ArrayList<String>) {
        resetCheckList()

        if (allHex.size > 0 && 8 * allHex.size < 44) {
            val diff = ((44 - (8 * allHex.size)) / 2)
            for (i in 0..10) {
                for (j in 0..diff) {
                    checkList!![i].list.add(false)
                }
            }
        }

        for (hex in allHex) {
            for (i in 0..10) {
                val bin = hexToBin(hex.substring(i * 2, i * 2 + 2))
                for (j in 0..7) {
                    checkList!![i].list.add(Character.getNumericValue(bin[j]) == 1)
                }
            }
        }
        invalidate()
    }

    companion object {

        internal fun hexToBin(s: String): String {
            val number = BigInteger(s, 16).toString(2)
            val sb = StringBuilder(number)
            for (i in 0 until 8 - number.length) {
                sb.insert(0, "0")
            }
            return sb.toString()
        }
    }
}
