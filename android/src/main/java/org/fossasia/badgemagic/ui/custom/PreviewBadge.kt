package org.fossasia.badgemagic.ui.custom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.Mode
import org.fossasia.badgemagic.data.Speed
import org.fossasia.badgemagic.data.badge_preview.Cell
import org.fossasia.badgemagic.data.badge_preview.CheckList
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.Converters.hexToBin

private const val BUNDLE_STATE = "superState"
private const val BUNDLE_FLASH = "ifFlash"
private const val BUNDLE_MARQUEE = "ifMarquee"
private const val BUNDLE_SPEED = "badgeSpeed"
private const val BUNDLE_MODE = "badgeMode"
private const val BUNDLE_CHECKLIST = "checkList"

class PreviewBadge : View {
    private val TAG = "PreviewBadge"
    private var ledDisabled: Drawable
    private var ledEnabled: Drawable

    private lateinit var bgBounds: RectF
    private var cells = mutableListOf<Cell>()

    private var badgeHeight = 11
    private var badgeWidth = 40
    private val maxOffset: Int by lazy { Converters.DpToPx(24, context) }
    private var oneByte = 8

    private var animationIndex: Int = 0

    private var ifFlash: Boolean = false
    private var ifMarquee: Boolean = false
    private var badgeSpeed: Int = 1
    private var badgeMode: Mode = Mode.LEFT

    private var checkList = ArrayList<CheckList>()

    private var valueAnimator: ValueAnimator? = ValueAnimator().apply {
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
    }

    private var countFrame = 0
    private var lastFrame = 0

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

        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val offset = (measuredWidth / 32).run { if (this > maxOffset) maxOffset else this }
        val singleCell = (originalWidth - 4 * offset) / badgeWidth

        val calculatedHeight = singleCell * badgeHeight + 4 * offset

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY))
    }

    public override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_STATE, super.onSaveInstanceState())
        bundle.putBoolean(BUNDLE_FLASH, this.ifFlash)
        bundle.putBoolean(BUNDLE_MARQUEE, this.ifMarquee)
        bundle.putInt(BUNDLE_SPEED, this.badgeSpeed)
        bundle.putInt(BUNDLE_MODE, this.badgeMode.ordinal)
        bundle.putParcelableArrayList(BUNDLE_CHECKLIST, this.checkList)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var currentState = state
        if (currentState is Bundle) {
            this.ifFlash = currentState.getBoolean(BUNDLE_FLASH)
            this.ifMarquee = currentState.getBoolean(BUNDLE_MARQUEE)
            this.badgeSpeed = currentState.getInt(BUNDLE_SPEED)
            this.badgeMode = Mode.values()[currentState.getInt(BUNDLE_MODE)]
            this.checkList = currentState.getParcelableArrayList(BUNDLE_CHECKLIST) ?: ArrayList()

            countFrame = 0
            lastFrame = 0

            currentState.getParcelable<Parcelable>(BUNDLE_STATE)?.let { currentState = it }
        }
        super.onRestoreInstanceState(currentState)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset: Float = (measuredWidth / 32f).run { if (this > maxOffset) maxOffset.toFloat() else this }
        Log.d(TAG, "onLayout: $offset $maxOffset")
        val singleCell = (measuredWidth - 4 * offset) / badgeWidth

        cells = mutableListOf()
        for (i in 0 until badgeHeight) {
            cells.add(Cell())
            for (j in 0 until badgeWidth) {
                cells[i].list.add(Rect(
                        ((offset * 2) + j * singleCell).toInt(),
                        ((offset * 2) + i * singleCell).toInt(),
                        ((offset * 2) + j * singleCell + singleCell).toInt(),
                        ((offset * 2) + i * singleCell + singleCell).toInt()
                ))
            }
        }

        bgBounds = RectF((offset).toFloat(), (offset).toFloat(), ((singleCell * badgeWidth) + (offset * 3)).toFloat(), ((singleCell * badgeHeight) + (offset * 3)).toFloat())
    }

    private fun drawLED(condition: Boolean, canvas: Canvas, xValue: Int, yValue: Int) {
        if (condition) {
            ledEnabled.bounds = cells[xValue].list[yValue]
            ledEnabled.draw(canvas)
        } else {
            ledDisabled.bounds = cells[xValue].list[yValue]
            ledDisabled.draw(canvas)
        }
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

        val pictureCheckList = ArrayList<CheckList>()
        if (badgeMode == Mode.PICTURE)
            for (k in badgeHeight * 2 - 1 downTo 0)
                if (k % 2 == 0)
                    pictureCheckList.add(checkList[k / 2])
                else {
                    val newList = CheckList()
                    for (l in 0 until checkList[0].list.size)
                        newList.list.add(false)
                    pictureCheckList.add(newList)
                }

        // Draw Cells
        for (i in 0 until badgeHeight) {
            var matchFrame = false
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
                        val leftCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkList[i].list[(checkListLength + j - animationValue).rem(checkListLength)]

                        drawLED(
                            leftCondition,
                            canvas,
                            i, j
                        )
                    }
                    Mode.RIGHT -> {
                        val animationValue = animationIndex.div(200)
                        val rightCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkList[i].list[(animationValue + j + checkListLength - badgeWidth / 2).rem(checkListLength)]

                        drawLED(
                            rightCondition,
                            canvas,
                            i, j
                        )
                    }
                    Mode.UP -> {
                        val animationValue = animationIndex.div(((checkList[0].list.size * 200).toDouble() / badgeHeight).toInt())
                        val upCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkList[(i - animationValue + checkListHeight).rem(checkListHeight)].list[j + badgeWidth / 2]

                        drawLED(
                            upCondition,
                            canvas,
                            i, j
                        )
                    }
                    Mode.DOWN -> {
                        val animationValue = animationIndex.div(((checkList[0].list.size * 200).toDouble() / badgeHeight).toInt())
                        val downCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkList[(animationValue + i + checkListHeight).rem(checkListHeight)].list[j + badgeWidth / 2]

                        drawLED(
                            downCondition,
                            canvas,
                            i, j
                        )
                    }
                    Mode.FIXED -> {
                        val fixedCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkList[i].list[j + badgeWidth / 2]

                        drawLED(
                            fixedCondition,
                            canvas,
                            i, j
                        )
                    }
                    Mode.SNOWFLAKE -> {
                        val flakeIndex = animationIndex.div(100).rem(badgeWidth / 2)

                        val offsetToAdd = (countFrame / (badgeWidth / 2)) * badgeWidth +
                            if ((countFrame / (badgeWidth / 2)) > 0)
                                (4 * (countFrame / (badgeWidth / 2)))
                            else
                                0

                        if (lastFrame != flakeIndex) {
                            countFrame += 1
                        }
                        lastFrame = flakeIndex

                        val snowflakeCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            j + badgeWidth / 2 + offsetToAdd < checkList[i].list.size &&
                            checkList[i].list[j + badgeWidth / 2 + offsetToAdd]

                        drawLED(
                            snowflakeCondition,
                            canvas,
                            i, j
                        )

                        if (countFrame >= ((checkList[0].list.size / badgeWidth) * (badgeWidth / 2))) {
                            countFrame = 0
                            lastFrame = 0
                        }
                    }
                    Mode.PICTURE -> {
                        val firstLine = (badgeHeight - 1) - animationIndex.div(800).rem(badgeHeight)
                        val virtualLine = animationIndex.div(800).rem(badgeHeight)

                        if (lastFrame != firstLine) {
                            countFrame += 1
                        }
                        lastFrame = firstLine

                        val pictureCondition = validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            when {
                                countFrame < badgeHeight ->
                                    i <= (badgeHeight - 1 - virtualLine) &&
                                        pictureCheckList[badgeHeight.minus(virtualLine.plus(i))].list[j + badgeWidth / 2]
                                countFrame < (badgeHeight * 2) ->
                                    if (i < virtualLine - 1)
                                        i < (2 * badgeHeight - 1 - virtualLine) && virtualLine > 2 &&
                                            pictureCheckList[badgeHeight.times(2).minus(virtualLine.plus(i).minus(1))].list[j + badgeWidth / 2]
                                    else
                                        checkList[i].list[j + badgeWidth / 2]
                                countFrame < (badgeHeight * 3) ->
                                    j < checkList[i].list.size &&
                                        checkList[i].list[j + badgeWidth / 2]
                                countFrame < (badgeHeight * 4) ->
                                    if (i > virtualLine)
                                        virtualLine in 0 until i &&
                                            pictureCheckList[badgeHeight.times(2).minus(virtualLine.plus(i).plus(1))].list[j + badgeWidth / 2]
                                    else
                                        checkList[i].list[j + badgeWidth / 2]
                                else ->
                                    i > firstLine &&
                                        pictureCheckList[badgeHeight.times(3).minus(1).minus(virtualLine.plus(i))].list[j + badgeWidth / 2]
                            }

                        drawLED(
                            pictureCondition,
                            canvas,
                            i, j
                        )

                        if (countFrame > (5 * (badgeHeight) - 1)) {
                            countFrame = 0
                            lastFrame = 0
                        }
                    }
                    Mode.ANIMATION -> {
                        val firstLine = animationIndex.div(200).rem(badgeWidth / 2)
                        val secondLine = badgeWidth - firstLine

                        if (lastFrame != firstLine)
                            countFrame += 1
                        lastFrame = firstLine

                        val checkLineOnRow = when {
                            countFrame < (badgeWidth / 2) || countFrame > (3 * (badgeWidth / 2)) -> j == firstLine || j == secondLine
                            else -> false
                        }
                        val checkBitmapOnRow = when {
                            countFrame < (badgeWidth / 2) -> j in (firstLine + 1) until secondLine
                            countFrame > (3 * (badgeWidth / 2)) -> j < firstLine || j > secondLine
                            else -> true
                        }

                        val animationCondition = checkLineOnRow || validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkBitmapOnRow &&
                            checkList[i].list[j + badgeWidth / 2]

                        drawLED(
                            animationCondition,
                            canvas,
                            i, j
                        )

                        if (countFrame > (4 * (badgeWidth / 2))) {
                            countFrame = 0
                            lastFrame = 0
                        }
                    }
                    Mode.LASER -> {
                        val line = badgeWidth - animationIndex.div(400).rem(badgeWidth)

                        if (lastFrame != line)
                            countFrame += 1
                        lastFrame = line

                        if (!matchFrame)
                            matchFrame = checkList[i].list[lastFrame + badgeWidth / 2]

                        val checkLineOnRow = when {
                            countFrame < (badgeWidth + 1) -> matchFrame && j >= lastFrame
                            countFrame > (2 * badgeWidth) -> matchFrame && j <= lastFrame
                            else -> false
                        }

                        val checkBitmapOnRow = when {
                            countFrame < (badgeWidth + 1) -> j < lastFrame
                            countFrame > (2 * badgeWidth) -> j > lastFrame
                            else -> true
                        }

                        val laserCondition = checkLineOnRow || validMarquee || flashLEDOn &&
                            i < checkList.size &&
                            j < checkList[i].list.size &&
                            checkBitmapOnRow &&
                            checkList[i].list[j + badgeWidth / 2]

                        drawLED(
                            laserCondition,
                            canvas,
                            i, j
                        )

                        if (countFrame > (3 * (badgeWidth))) {
                            countFrame = 0
                            lastFrame = 0
                        }
                    }
                }
            }
        }

        postInvalidateOnAnimation()
    }

    override fun onDetachedFromWindow() {
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (badgeMode != Mode.PICTURE)
            configValueAnimation(checkList[0].list.size * 200)
        else
            configValueAnimation()
    }

    private fun configValueAnimation(valueAnimatorNumber: Int = 8800) {
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
        valueAnimator?.addUpdateListener {
            animationIndex = it.animatedValue as Int
        }
        valueAnimator?.setIntValues(valueAnimatorNumber - 1, 0)
        valueAnimator?.duration = valueAnimatorNumber.toLong().div(badgeSpeed)
        valueAnimator?.start()
    }

    fun setValue(allHex: List<String>, ifMar: Boolean, ifFla: Boolean, speed: Speed, mode: Mode) {
        resetCheckList()
        ifMarquee = ifMar
        ifFlash = ifFla

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

        for (i in 0 until badgeHeight) {
            for (j in (0 until badgeWidth / 2)) {
                checkList[i].list.add(false)
            }
        }

        val diff = (badgeWidth - (oneByte * allHex.size)) / 2
        if (oneByte * allHex.size < badgeWidth && mode != Mode.SNOWFLAKE) {
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
        for (i in 0 until badgeHeight) {
            for (j in 0 until badgeWidth / 2) {
                checkList[i].list.add(false)
            }
        }

        countFrame = 0
        lastFrame = 0

        invalidate()

        if (badgeMode != Mode.PICTURE)
            configValueAnimation(checkList[0].list.size * 200)
        else
            configValueAnimation()
    }
}
