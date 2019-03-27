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
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class PreviewBadge : View {
    private var ledDisabled: Drawable? = null
    private var ledEnabled: Drawable? = null

    private lateinit var bgBounds: RectF
    private var cells = ArrayList<Cell>()

    private var badgeHeight = 11
    private var badgeWidth = 44

    private var ifFlash: Boolean? = false
    private var ifMarquee: Boolean? = false

    private var animationIndex: Int = 0

    private var checkList: ArrayList<CheckList>? = null
    private var valueAnimator: ValueAnimator? = null

    private fun resetCheckList() {
        checkList = ArrayList()
        for (i in 0 until badgeHeight) {
            checkList?.add(CheckList())
        }
    }

    constructor(context: Context) : super(context) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        if (checkList == null)
            resetCheckList()
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset = 30

        val singleCell = (right - left - (offset * 3)) / badgeWidth

        cells = ArrayList()
        for (i in 0 until badgeHeight) {
            cells.add(Cell())
            for (j in 0 until badgeWidth) {
                cells[i].list.add(Rect(
                        left + (offset * 2) + j * singleCell,
                        top + (offset * 2) + i * singleCell,
                        left + (offset * 2) + j * singleCell + singleCell,
                        top + (offset * 2) + i * singleCell + singleCell
                ))
            }
        }
        bgBounds = RectF((left + offset).toFloat(), (top + offset).toFloat(), (right - offset).toFloat(), ((singleCell * badgeHeight) + (offset * 3)).toFloat())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Paint Configuration
        val bgPaint = Paint()
        bgPaint.isAntiAlias = true
        bgPaint.color = Color.parseColor("#000000")

        // Draw Background
        canvas.drawRoundRect(bgBounds, 25f, 25f, bgPaint)

        // Draw Cells
        for (i in 0 until badgeHeight) {
            for (j in 0 until badgeWidth) {
                if (ifFlash == true) {
                    if (animationIndex > 100 && i < checkList?.size ?: -1 && j < checkList?.get(i)?.list?.size ?: -1 && checkList?.get(i)?.list?.get(j) == true) {
                        ledEnabled?.bounds = cells[i].list[j]
                        ledEnabled?.draw(canvas)
                    } else {
                        ledDisabled?.bounds = cells[i].list[j]
                        ledDisabled?.draw(canvas)
                    }
                } else if (ifMarquee == true) {
                    if (animationIndex < 0)
                        animationIndex = 0
                    if (i < checkList?.size ?: -1 && j < checkList?.get(i)?.list?.size ?: -1 && j >= animationIndex.div(200) && checkList?.get(i)?.list?.get(j - animationIndex.div(200)) == true) {
                        ledEnabled?.bounds = cells[i].list[j]
                        ledEnabled?.draw(canvas)
                    } else {
                        ledDisabled?.bounds = cells[i].list[j]
                        ledDisabled?.draw(canvas)
                    }
                } else {
                    if (i < checkList?.size ?: -1 && j < checkList?.get(i)?.list?.size ?: -1 && checkList?.get(i)?.list?.get(j) == true) {
                        ledEnabled?.bounds = cells[i].list[j]
                        ledEnabled?.draw(canvas)
                    } else {
                        ledDisabled?.bounds = cells[i].list[j]
                        ledDisabled?.draw(canvas)
                    }
                }
            }
        }

        if (ifFlash == true || ifMarquee == true) {
            postInvalidateOnAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        valueAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (ifFlash == true) {
            valueAnimator = ValueAnimator.ofInt(0, 200).apply {
                addUpdateListener {
                    animationIndex = it.animatedValue as Int
                }
                duration = 1500L
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                start()
            }
        } else if (ifMarquee == true) {
            valueAnimator = ValueAnimator.ofInt(8799, -1000).apply {
                addUpdateListener {
                    animationIndex = it.animatedValue as Int
                }
                duration = 4400L
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    fun setValue(allHex: ArrayList<String>, ifMar: Boolean, ifFla: Boolean) {
        resetCheckList()
        ifMarquee = ifMar
        ifFlash = ifFla
        valueAnimator?.cancel()
        if (ifFlash == true) {
            valueAnimator = ValueAnimator.ofInt(0, 200).apply {
                addUpdateListener {
                    animationIndex = it.animatedValue as Int
                }
                duration = 1500L
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                start()
            }
        } else if (ifMarquee == true) {
            valueAnimator = ValueAnimator.ofInt(8799, -1000).apply {
                addUpdateListener {
                    animationIndex = it.animatedValue as Int
                }
                duration = 4400L
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                start()
            }
        }

        val diff: Int
        if (allHex.size > 0) {
            diff = ((badgeWidth - (8 * allHex.size)) / 2)
            if (8 * allHex.size < badgeWidth) {
                for (i in 0 until badgeHeight) {
                    for (j in 0 until diff) {
                        checkList?.get(i)?.list?.add(false)
                    }
                }
            }
        } else {
            diff = 22
            for (i in 0 until badgeHeight) {
                for (j in 0 until diff) {
                    checkList?.get(i)?.list?.add(false)
                }
            }
        }
        for (hex in allHex) {
            for (i in 0 until badgeHeight) {
                val bin = hexToBin(hex.substring(i * 2, i * 2 + 2))
                for (j in 0..7) {
                    checkList?.get(i)?.list?.add(Character.getNumericValue(bin[j]) == 1)
                }
            }
        }
        for (i in 0 until badgeHeight) {
            for (j in 0 until diff) {
                checkList?.get(i)?.list?.add(false)
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
