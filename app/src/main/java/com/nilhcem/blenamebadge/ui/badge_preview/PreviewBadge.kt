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

import java.math.BigInteger
import java.util.ArrayList
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed

class PreviewBadge : View {
    private var ledDisabled: Drawable
    private var ledEnabled: Drawable

    private lateinit var bgBounds: RectF
    private var cells = ArrayList<Cell>()

    private var badgeHeight = 11
    private var badgeWidth = 44

    private var oneByte = 8

    private var ifFlash: Boolean = false
    private var ifMarquee: Boolean = false
    private var badgeSpeed: Int = 1
    private var badgeMode: Mode = Mode.LEFT

    private var animationIndex: Int = 0

    private var checkList: ArrayList<CheckList> = ArrayList()
    private var valueAnimator: ValueAnimator? = null

    private fun resetCheckList() {
        checkList = ArrayList()
        for (i in 0 until badgeHeight) {
            checkList.add(CheckList())
        }
    }

    constructor(context: Context) : super(context) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        resetCheckList()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        resetCheckList()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        ledDisabled = context.resources.getDrawable(R.drawable.ic_led)
        ledEnabled = context.resources.getDrawable(R.drawable.ic_led_lit)

        resetCheckList()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val ratioHeight = 1
        val ratioWidth = 3

        val originalWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val calculatedHeight = originalWidth * ratioHeight / ratioWidth

        setMeasuredDimension(
                View.MeasureSpec.makeMeasureSpec(originalWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(calculatedHeight, View.MeasureSpec.EXACTLY))
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset = 30

        val singleCell = (right - left - (offset * 3)) / badgeWidth

        val offsetXToAdd: Int = ((((right - offset).toFloat() - (left + offset).toFloat()) - (singleCell * badgeWidth)) / 2).toInt() + 1

        cells = ArrayList()
        for (i in 0 until badgeHeight) {
            cells.add(Cell())
            for (j in 0 until badgeWidth) {
                cells[i].list.add(Rect(
                        left + (offsetXToAdd * 2) + j * singleCell,
                        top + (offsetXToAdd * 2) + i * singleCell,
                        left + (offsetXToAdd * 2) + j * singleCell + singleCell,
                        top + (offsetXToAdd * 2) + i * singleCell + singleCell
                ))
            }
        }
        bgBounds = RectF((left + offset).toFloat(), (top + offset).toFloat(), (right - offset).toFloat(), ((singleCell * badgeHeight) + (offsetXToAdd * 3)).toFloat())
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
                var flashLEDOn = true
                if (ifFlash) {
                    val aIFlash = animationIndex % 800
                    flashLEDOn = aIFlash > 400
                }
                var validMarquee = false
                if (ifMarquee) {
                    val aIMarquee = animationIndex.div(200)
                    validMarquee = if (i == 0 || j == 0 || i == badgeHeight - 1 || j == badgeWidth - 1) {
                        if ((i == 0 || j == badgeWidth - 1) && !(i == badgeHeight - 1 && j == badgeWidth - 1)) {
                            (i + j) % 4 == (aIMarquee % 4)
                        } else {
                            (i + j - 1) % 4 == (3 - (aIMarquee % 4))
                        }
                    } else {
                        false
                    }
                }

                val checkListLength = checkList[i].list.size
                val checkListHeight = checkList.size
                when (badgeMode) {
                    Mode.LEFT -> {
                        val animationValue = animationIndex.div(200)
                        if (validMarquee || flashLEDOn &&
                                i < checkList.size &&
                                j < checkList[i].list.size &&
                                checkList[i].list[(checkListLength + j - animationValue).rem(checkListLength)]) {
                            ledEnabled.bounds = cells[i].list[j]
                            ledEnabled.draw(canvas)
                        } else {
                            ledDisabled.bounds = cells[i].list[j]
                            ledDisabled.draw(canvas)
                        }
                    }
                    Mode.RIGHT -> {
                        val animationValue = animationIndex.div(200)
                        if (validMarquee || flashLEDOn &&
                                i < checkList.size &&
                                j < checkList[i].list.size &&
                                checkList[i].list[(animationValue + j + checkListLength).rem(checkListLength)]) {
                            ledEnabled.bounds = cells[i].list[j]
                            ledEnabled.draw(canvas)
                        } else {
                            ledDisabled.bounds = cells[i].list[j]
                            ledDisabled.draw(canvas)
                        }
                    }
                    Mode.UP -> {
                        val animationValue = animationIndex.div(((checkList[0].list.size*200).toDouble() / badgeHeight).toInt())
                        if (validMarquee || flashLEDOn &&
                                i < checkList.size &&
                                j < checkList[i].list.size &&
                                checkList[(i - animationValue + checkListHeight).rem(checkListHeight)].list[j]) {
                            ledEnabled.bounds = cells[i].list[j]
                            ledEnabled.draw(canvas)
                        } else {
                            ledDisabled.bounds = cells[i].list[j]
                            ledDisabled.draw(canvas)
                        }
                    }
                    Mode.DOWN -> {
                        val animationValue = animationIndex.div(((checkList[0].list.size*200).toDouble() / badgeHeight).toInt())
                        if (validMarquee || flashLEDOn &&
                                i < checkList.size &&
                                j < checkList[i].list.size &&
                                checkList[(animationValue + i + checkListHeight).rem(checkListHeight)].list[j]) {
                            ledEnabled.bounds = cells[i].list[j]
                            ledEnabled.draw(canvas)
                        } else {
                            ledDisabled.bounds = cells[i].list[j]
                            ledDisabled.draw(canvas)
                        }
                    }
                    Mode.FIXED -> {
                        if (validMarquee || flashLEDOn &&
                                i < checkList.size &&
                                j < checkList[i].list.size &&
                                checkList[i].list[j]) {
                            ledEnabled.bounds = cells[i].list[j]
                            ledEnabled.draw(canvas)
                        } else {
                            ledDisabled.bounds = cells[i].list[j]
                            ledDisabled.draw(canvas)
                        }
                    }
                    Mode.SNOWFLAKE -> {
                    }
                    Mode.PICTURE -> {
                    }
                    Mode.ANIMATION -> {
                    }
                    Mode.LASER -> {
                    }
                }
            }
        }

        postInvalidateOnAnimation()
    }

    override fun onDetachedFromWindow() {
        valueAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        configValueAnimation()
    }

    private fun configValueAnimation(valueAnimatorNumber: Int = 8800) {
        valueAnimator = ValueAnimator.ofInt(valueAnimatorNumber - 1, 0).apply {
            addUpdateListener {
                animationIndex = it.animatedValue as Int
            }
            duration = valueAnimatorNumber.toLong().div(badgeSpeed)
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    fun setValue(allHex: ArrayList<String>, ifMar: Boolean, ifFla: Boolean, speed: Speed, mode: Mode) {
        resetCheckList()
        ifMarquee = ifMar
        ifFlash = ifFla
        valueAnimator?.cancel()

        badgeMode = mode
        badgeSpeed = when (speed) {
            Speed.ONE -> 1
            Speed.TWO -> 2
            Speed.THREE -> 3
            Speed.FOUR -> 4
            Speed.FIVE -> 5
            Speed.SIX -> 6
            Speed.SEVEN -> 7
            Speed.EIGHT -> 8
        }

        val diff = (badgeWidth - (oneByte * allHex.size)) / 2
        if (diff < 0) {
            for (i in 0 until badgeHeight) {
                for (j in (0 until badgeWidth / 2)) {
                    checkList[i].list.add(false)
                }
            }
        }
        if (oneByte * allHex.size < badgeWidth) {
            for (i in 0 until badgeHeight) {
                for (j in 0 until diff) {
                    checkList[i].list.add(false)
                }
            }
        }
        for (hex in allHex) {
            for (i in 0 until badgeHeight) {
                val bin = hexToBin(hex.substring(i * 2, i * 2 + 2))
                for (j in 0 until oneByte) {
                    checkList[i].list.add(Character.getNumericValue(bin[j]) == 1)
                }
            }
        }
        for (i in 0 until badgeHeight) {
            for (j in 0 until diff) {
                checkList[i].list.add(false)
            }
        }
        if (diff < 0) {
            for (i in 0 until badgeHeight) {
                for (j in 0 until badgeWidth / 2) {
                    checkList[i].list.add(false)
                }
            }
        }
        invalidate()
        configValueAnimation(checkList[0].list.size*200)
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
