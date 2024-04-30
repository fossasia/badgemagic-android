package org.fossasia.badgemagic.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.badge_preview.Cell
import org.fossasia.badgemagic.data.badge_preview.CheckList
import org.fossasia.badgemagic.data.draw_layout.DrawMode
import org.fossasia.badgemagic.util.Converters

class DrawBadgeLayout(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var ledDisabled: Drawable? = context?.getDrawable(R.drawable.ic_led)
    private var ledEnabled: Drawable? = context?.getDrawable(R.drawable.ic_led_lit)

    private lateinit var bgBounds: RectF

    private val oneByte = 8

    private var badgeHeight = 11
    private var badgeWidth = 44

    private var cells = mutableListOf<Cell>()

    private var checkList = ArrayList<CheckList>()

    private var drawMode = DrawMode.NOTHING

    init {
        resetCheckListWithDummyData()
    }

    fun changeDrawState(mode: DrawMode) {
        drawMode = mode
    }

    fun getCheckedList() = checkList

    private fun resetCheckList() {
        checkList = ArrayList()
        for (i in 0 until badgeHeight) {
            checkList.add(CheckList())
        }
    }

    fun resetCheckListWithDummyData() {
        resetCheckList()
        for (i in 0 until badgeHeight) {
            for (j in 0 until badgeWidth) {
                checkList[i].list.add(false)
            }
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val ratioHeight = 1
        val ratioWidth = 3

        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val calculatedHeight = originalWidth * ratioHeight / ratioWidth

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY)
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset = 30

        val singleCell = (right - left - (offset * 3)) / badgeWidth

        val offsetXToAdd: Int = ((((right - offset).toFloat() - (left + offset).toFloat()) - (singleCell * badgeWidth)) / 2).toInt() + 1

        cells = mutableListOf()
        for (i in 0 until badgeHeight) {
            cells.add(Cell())
            for (j in 0 until badgeWidth) {
                cells[i].list.add(
                    Rect(
                        (offsetXToAdd * 2) + j * singleCell,
                        (offsetXToAdd * 2) + i * singleCell,
                        (offsetXToAdd * 2) + j * singleCell + singleCell,
                        (offsetXToAdd * 2) + i * singleCell + singleCell
                    )
                )
            }
        }

        bgBounds = RectF((offsetXToAdd).toFloat(), (offsetXToAdd).toFloat(), ((singleCell * badgeWidth) + (offsetXToAdd * 3)).toFloat(), ((singleCell * badgeHeight) + (offsetXToAdd * 3)).toFloat())
    }

    private fun drawLED(condition: Boolean, canvas: Canvas, xValue: Int, yValue: Int) {
        if (condition) {
            ledEnabled?.bounds = cells[xValue].list[yValue]
            ledEnabled?.draw(canvas)
        } else {
            ledDisabled?.bounds = cells[xValue].list[yValue]
            ledDisabled?.draw(canvas)
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
        for (i in 0 until badgeHeight) {
            for (j in 0 until badgeWidth) {
                drawLED(
                    checkList[i].list[j],
                    canvas,
                    i, j
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        changeCheckList(event?.x, event?.y)
        return true
    }

    private fun changeCheckList(x: Float?, y: Float?) {
        if (x != null && y != null && drawMode != DrawMode.NOTHING) {
            if (liesWithinX(x) || liesWithinY(y))
                return

            val singleCellSize = cells[0].list[0].right - cells[0].list[0].left
            val newX = x - cells[0].list[0].left
            val newY = y - cells[0].list[0].top

            checkList[(newY / singleCellSize).toInt()].list[(newX / singleCellSize).toInt()] = when (drawMode) {
                DrawMode.DRAW -> true
                DrawMode.ERASE -> false
                else -> false
            }

            invalidate()
        }
    }

    private fun liesWithinX(x: Float) = x <= cells[0].list[0].left || x >= cells[0].list[badgeWidth - 1].right
    private fun liesWithinY(y: Float) = y <= cells[0].list[0].top || y >= cells[badgeHeight - 1].list[0].bottom

    fun setValue(hexStrings: List<String>) {
        resetCheckList()
        for (hex in hexStrings) {
            for (i in 0 until badgeHeight) {
                val bin = Converters.hexToBin(hex.substring(i * 2, i * 2 + 2))
                for (j in 0 until oneByte) {
                    checkList[i].list.add(Character.getNumericValue(bin[j]) == 1)
                }
            }
        }
        val diff = (badgeWidth - (oneByte * hexStrings.size))
        for (i in 0 until badgeHeight) {
            for (j in 0 until diff) {
                checkList[i].list.add(false)
            }
        }
        invalidate()
    }
}
