package org.fossasia.badgemagic.ui.custom.knob

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.ui.custom.knob.utilities.Utils

class Croller : View {

    private var midx: Float = 0.toFloat()
    private var midy: Float = 0.toFloat()
    private var textPaint: Paint = Paint()
    private var circlePaint: Paint = Paint()
    private var circlePaint2: Paint = Paint()
    private var linePaint: Paint = Paint()
    private var currdeg = 0f
    private var deg = 3f
    private var downdeg = 0f

    private var isContinuous = false
        set(isContinuous) {
            field = isContinuous
            invalidate()
        }

    private var backCircleColor = Color.parseColor("#222222")
        set(backCircleColor) {
            field = backCircleColor
            invalidate()
        }
    private var mainCircleColor = Color.parseColor("#000000")
        set(mainCircleColor) {
            field = mainCircleColor
            invalidate()
        }
    private var indicatorColor = Color.parseColor("#FFA036")
        set(indicatorColor) {
            field = indicatorColor
            invalidate()
        }
    private var progressPrimaryColor = Color.parseColor("#FFA036")
        set(progressPrimaryColor) {
            field = progressPrimaryColor
            invalidate()
        }
    private var progressSecondaryColor = Color.parseColor("#111111")
        set(progressSecondaryColor) {
            field = progressSecondaryColor
            invalidate()
        }

    private var backCircleDisabledColor = Color.parseColor("#82222222")
        set(backCircleDisabledColor) {
            field = backCircleDisabledColor
            invalidate()
        }
    private var mainCircleDisabledColor = Color.parseColor("#82000000")
        set(mainCircleDisabledColor) {
            field = mainCircleDisabledColor
            invalidate()
        }
    private var indicatorDisabledColor = Color.parseColor("#82FFA036")
        set(indicatorDisabledColor) {
            field = indicatorDisabledColor
            invalidate()
        }
    private var progressPrimaryDisabledColor = Color.parseColor("#82FFA036")
        set(progressPrimaryDisabledColor) {
            field = progressPrimaryDisabledColor
            invalidate()
        }
    private var progressSecondaryDisabledColor = Color.parseColor("#82111111")
        set(progressSecondaryDisabledColor) {
            field = progressSecondaryDisabledColor
            invalidate()
        }

    private var progressPrimaryCircleSize = -1f
        set(progressPrimaryCircleSize) {
            field = progressPrimaryCircleSize
            invalidate()
        }
    private var progressSecondaryCircleSize = -1f
        set(progressSecondaryCircleSize) {
            field = progressSecondaryCircleSize
            invalidate()
        }

    private var progressPrimaryStrokeWidth = 25f
        set(progressPrimaryStrokeWidth) {
            field = progressPrimaryStrokeWidth
            invalidate()
        }
    private var progressSecondaryStrokeWidth = 10f
        set(progressSecondaryStrokeWidth) {
            field = progressSecondaryStrokeWidth
            invalidate()
        }

    private var mainCircleRadius = -1f
    private var backCircleRadius = -1f
    private var progressRadius = -1f

    private var max = 25
        set(max) {
            field = if (max < this.min) {
                this.min
            } else {
                max
            }
            invalidate()
        }
    private var min = 1
        set(min) {
            field = when {
                min < 0 -> 0
                min > this.max -> this.max
                else -> min
            }
            invalidate()
        }

    private var indicatorWidth = 7f
        set(indicatorWidth) {
            field = indicatorWidth
            invalidate()
        }

    private var label: String? = "Label"
        set(txt) {
            field = txt
            invalidate()
        }
    private var labelFont: String? = null
        set(labelFont) {
            field = labelFont
            generateTypeface()
            invalidate()
        }
    private var labelStyle = 0
        set(labelStyle) {
            field = labelStyle
            invalidate()
        }
    private var labelSize = 14f
        set(labelSize) {
            field = labelSize
            invalidate()
        }
    private var labelColor = Color.WHITE
        set(labelColor) {
            field = labelColor
            invalidate()
        }

    private var labelDisabledColor = Color.BLACK

    private var startOffset = 30
        set(startOffset) {
            field = startOffset
            invalidate()
        }
    private var startOffset2 = 0
    private var sweepAngle = -1

    private var isEnabled = true

    private var isAntiClockwise = false
        set(antiClockwise) {
            field = antiClockwise
            invalidate()
        }

    private var startEventSent = false

    private lateinit var oval: RectF

    private var progressChangeListener: OnProgressChangedListener? = null
    private var crollerChangeListener: OnCrollerChangeListener? = null

    internal var progress: Int
        get() = (deg - 2).toInt()
        set(x) {
            deg = (x + 2).toFloat()
            invalidate()
        }

    interface OnProgressChangedListener {
        fun onProgressChanged(progress: Int)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initXMLAttrs(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initXMLAttrs(context, attrs)
        init()
    }

    private fun init() {

        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.isFakeBoldText = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = this.labelSize

        generateTypeface()

        circlePaint = Paint()
        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = this.progressSecondaryStrokeWidth
        circlePaint.style = Paint.Style.FILL

        circlePaint2 = Paint()
        circlePaint2.isAntiAlias = true
        circlePaint2.strokeWidth = this.progressPrimaryStrokeWidth
        circlePaint2.style = Paint.Style.FILL

        linePaint = Paint()
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = this.indicatorWidth

        if (isEnabled) {
            circlePaint2.color = this.progressPrimaryColor
            circlePaint.color = this.progressSecondaryColor
            linePaint.color = this.indicatorColor
            textPaint.color = this.labelColor
        } else {
            circlePaint2.color = this.progressPrimaryDisabledColor
            circlePaint.color = this.progressSecondaryDisabledColor
            linePaint.color = this.indicatorDisabledColor
            textPaint.color = labelDisabledColor
        }

        oval = RectF()
    }

    private fun generateTypeface() {
        var plainLabel = Typeface.DEFAULT
        if (labelFont != null && labelFont?.isNotEmpty() == true) {
            val assetMgr = context.assets
            plainLabel = Typeface.createFromAsset(assetMgr, labelFont)
        }

        when (labelStyle) {
            0 -> textPaint.typeface = plainLabel
            1 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.BOLD)
            2 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.ITALIC)
            3 -> textPaint.typeface = Typeface.create(plainLabel, Typeface.BOLD_ITALIC)
        }
    }

    private fun initXMLAttrs(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.Croller)

        setEnabled(a.getBoolean(R.styleable.Croller_enabled, true))
        progress = a.getInt(R.styleable.Croller_start_progress, 1)
        label = a.getString(R.styleable.Croller_label)

        backCircleColor = a.getColor(R.styleable.Croller_back_circle_color, this.backCircleColor)
        mainCircleColor = a.getColor(R.styleable.Croller_main_circle_color, this.mainCircleColor)
        indicatorColor = a.getColor(R.styleable.Croller_indicator_color, this.indicatorColor)
        progressPrimaryColor = a.getColor(R.styleable.Croller_progress_primary_color, this.progressPrimaryColor)
        progressSecondaryColor = a.getColor(R.styleable.Croller_progress_secondary_color, this.progressSecondaryColor)

        backCircleDisabledColor = a.getColor(R.styleable.Croller_back_circle_disable_color, this.backCircleDisabledColor)
        mainCircleDisabledColor = a.getColor(R.styleable.Croller_main_circle_disable_color, this.mainCircleDisabledColor)
        indicatorDisabledColor = a.getColor(R.styleable.Croller_indicator_disable_color, this.indicatorDisabledColor)
        progressPrimaryDisabledColor = a.getColor(R.styleable.Croller_progress_primary_disable_color, this.progressPrimaryDisabledColor)
        progressSecondaryDisabledColor = a.getColor(R.styleable.Croller_progress_secondary_disable_color, this.progressSecondaryDisabledColor)

        labelSize = a.getDimension(R.styleable.Croller_label_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this.labelSize, resources.displayMetrics).toInt().toFloat())
        labelColor = a.getColor(R.styleable.Croller_label_color, this.labelColor)
        setlabelDisabledColor(a.getColor(R.styleable.Croller_label_disabled_color, labelDisabledColor))
        labelFont = a.getString(R.styleable.Croller_label_font)
        labelStyle = a.getInt(R.styleable.Croller_label_style, 0)
        indicatorWidth = a.getFloat(R.styleable.Croller_indicator_width, 7f)
        isContinuous = a.getBoolean(R.styleable.Croller_is_continuous, false)
        progressPrimaryCircleSize = a.getFloat(R.styleable.Croller_progress_primary_circle_size, -1f)
        progressSecondaryCircleSize = a.getFloat(R.styleable.Croller_progress_secondary_circle_size, -1f)
        progressPrimaryStrokeWidth = a.getFloat(R.styleable.Croller_progress_primary_stroke_width, 25f)
        progressSecondaryStrokeWidth = a.getFloat(R.styleable.Croller_progress_secondary_stroke_width, 10f)
        setSweepAngle(a.getInt(R.styleable.Croller_sweep_angle, -1))
        startOffset = a.getInt(R.styleable.Croller_start_offset, 30)
        max = a.getInt(R.styleable.Croller_max, 25)
        min = a.getInt(R.styleable.Croller_min, 1)
        deg = (this.min + 2).toFloat()
        setBackCircleRadius(a.getFloat(R.styleable.Croller_back_circle_radius, -1f))
        setProgressRadius(a.getFloat(R.styleable.Croller_progress_radius, -1f))
        isAntiClockwise = a.getBoolean(R.styleable.Croller_anticlockwise, false)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minWidth = Utils.convertDpToPixel(160f, context).toInt()
        val minHeight = Utils.convertDpToPixel(160f, context).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width: Int
        var height: Int

        width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(minWidth, widthSize)
            else -> // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
                // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
                heightSize
        }

        height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(minHeight, heightSize)
            else -> // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
                // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
                widthSize
        }

        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth
            height = minHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        midx = (width / 2).toFloat()
        midy = (height / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        progressChangeListener?.onProgressChanged((deg - 2).toInt())

        crollerChangeListener?.onProgressChanged(this, (deg - 2).toInt())

        if (isEnabled) {
            circlePaint2.color = this.progressPrimaryColor
            circlePaint.color = this.progressSecondaryColor
            linePaint.color = this.indicatorColor
            textPaint.color = this.labelColor
        } else {
            circlePaint2.color = this.progressPrimaryDisabledColor
            circlePaint.color = this.progressSecondaryDisabledColor
            linePaint.color = this.indicatorDisabledColor
            textPaint.color = labelDisabledColor
        }

        if (!this.isContinuous) {

            startOffset2 = this.startOffset - 15

            linePaint.strokeWidth = this.indicatorWidth
            textPaint.textSize = this.labelSize

            val radius = (Math.min(midx, midy) * (14.5.toFloat() / 16)).toInt()

            if (sweepAngle == -1) {
                sweepAngle = 360 - 2 * startOffset2
            }

            if (mainCircleRadius == -1f) {
                mainCircleRadius = radius * (11.toFloat() / 15)
            }
            if (backCircleRadius == -1f) {
                backCircleRadius = radius * (13.toFloat() / 15)
            }
            if (progressRadius == -1f) {
                progressRadius = radius.toFloat()
            }

            var x: Float
            var y: Float
            val deg2 = Math.max(3f, deg)
            val deg3 = Math.min(deg, (this.max + 2).toFloat())
            for (i in deg2.toInt() until this.max + 3) {
                var tmp = startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * i.toFloat() / (this.max + 5)

                if (this.isAntiClockwise) {
                    tmp = 1.0f - tmp
                }

                x = midx + (progressRadius * Math.sin(2.0 * Math.PI * (1.0 - tmp))).toFloat()
                y = midy + (progressRadius * Math.cos(2.0 * Math.PI * (1.0 - tmp))).toFloat()
                if (this.progressSecondaryCircleSize == -1f)
                    canvas.drawCircle(x, y, radius.toFloat() / 30 * (20.toFloat() / this.max) * (sweepAngle.toFloat() / 270), circlePaint)
                else
                    canvas.drawCircle(x, y, this.progressSecondaryCircleSize, circlePaint)
            }
            var i = 3
            while (i <= deg3) {
                var tmp = startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * i.toFloat() / (this.max + 5)

                if (this.isAntiClockwise) {
                    tmp = 1.0f - tmp
                }

                x = midx + (progressRadius * Math.sin(2.0 * Math.PI * (1.0 - tmp))).toFloat()
                y = midy + (progressRadius * Math.cos(2.0 * Math.PI * (1.0 - tmp))).toFloat()
                if (this.progressPrimaryCircleSize == -1f)
                    canvas.drawCircle(x, y, progressRadius / 15 * (20.toFloat() / this.max) * (sweepAngle.toFloat() / 270), circlePaint2)
                else
                    canvas.drawCircle(x, y, this.progressPrimaryCircleSize, circlePaint2)
                i++
            }

            var tmp2 = startOffset2.toFloat() / 360 + sweepAngle.toFloat() / 360 * deg / (this.max + 5)

            if (this.isAntiClockwise) {
                tmp2 = 1.0f - tmp2
            }

            val x1 = midx + (radius.toDouble() * (2.toFloat() / 5).toDouble() * Math.sin(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val y1 = midy + (radius.toDouble() * (2.toFloat() / 5).toDouble() * Math.cos(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val x2 = midx + (radius.toDouble() * (3.toFloat() / 5).toDouble() * Math.sin(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val y2 = midy + (radius.toDouble() * (3.toFloat() / 5).toDouble() * Math.cos(2.0 * Math.PI * (1.0 - tmp2))).toFloat()

            if (isEnabled)
                circlePaint.color = this.backCircleColor
            else
                circlePaint.color = this.backCircleDisabledColor
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint)
            if (isEnabled)
                circlePaint.color = this.mainCircleColor
            else
                circlePaint.color = this.mainCircleDisabledColor
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint)
            canvas.drawText(this.label
                ?: "", midx, midy + (radius * 1.1).toFloat() - textPaint.fontMetrics.descent, textPaint)
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        } else {

            val radius = (Math.min(midx, midy) * (14.5.toFloat() / 16)).toInt()

            if (sweepAngle == -1) {
                sweepAngle = 360 - 2 * this.startOffset
            }

            if (mainCircleRadius == -1f) {
                mainCircleRadius = radius * (11.toFloat() / 15)
            }
            if (backCircleRadius == -1f) {
                backCircleRadius = radius * (13.toFloat() / 15)
            }
            if (progressRadius == -1f) {
                progressRadius = radius.toFloat()
            }

            circlePaint.strokeWidth = this.progressSecondaryStrokeWidth
            circlePaint.style = Paint.Style.STROKE
            circlePaint2.strokeWidth = this.progressPrimaryStrokeWidth
            circlePaint2.style = Paint.Style.STROKE
            linePaint.strokeWidth = this.indicatorWidth
            textPaint.textSize = this.labelSize

            val deg3 = Math.min(deg, (this.max + 2).toFloat())

            oval.set(midx - progressRadius, midy - progressRadius, midx + progressRadius, midy + progressRadius)

            canvas.drawArc(oval, 90.toFloat() + this.startOffset, sweepAngle.toFloat(), false, circlePaint)
            if (this.isAntiClockwise) {
                canvas.drawArc(oval, 90.toFloat() - this.startOffset, -1 * ((deg3 - 2) * (sweepAngle.toFloat() / this.max)), false, circlePaint2)
            } else {
                canvas.drawArc(oval, 90.toFloat() + this.startOffset, (deg3 - 2) * (sweepAngle.toFloat() / this.max), false, circlePaint2)
            }

            var tmp2 = this.startOffset.toFloat() / 360 + sweepAngle.toFloat() / 360 * ((deg - 2) / this.max)

            if (this.isAntiClockwise) {
                tmp2 = 1.0f - tmp2
            }

            val x1 = midx + (radius.toDouble() * (2.toFloat() / 5).toDouble() * Math.sin(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val y1 = midy + (radius.toDouble() * (2.toFloat() / 5).toDouble() * Math.cos(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val x2 = midx + (radius.toDouble() * (3.toFloat() / 5).toDouble() * Math.sin(2.0 * Math.PI * (1.0 - tmp2))).toFloat()
            val y2 = midy + (radius.toDouble() * (3.toFloat() / 5).toDouble() * Math.cos(2.0 * Math.PI * (1.0 - tmp2))).toFloat()

            circlePaint.style = Paint.Style.FILL

            if (isEnabled)
                circlePaint.color = this.backCircleColor
            else
                circlePaint.color = this.backCircleDisabledColor
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint)
            if (isEnabled)
                circlePaint.color = this.mainCircleColor
            else
                circlePaint.color = this.mainCircleDisabledColor
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint)
            canvas.drawText(this.label
                ?: "", midx, midy + (radius * 1.1).toFloat() - textPaint.fontMetrics.descent, textPaint)
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {

        if (!isEnabled)
            return false

        if (Utils.getDistance(e.x, e.y, midx, midy) > Math.max(mainCircleRadius, Math.max(backCircleRadius, progressRadius))) {
            if (startEventSent && crollerChangeListener != null) {
                crollerChangeListener?.onStopTrackingTouch(this)
                startEventSent = false
            }
            return super.onTouchEvent(e)
        }

        if (e.action == MotionEvent.ACTION_DOWN) {

            val dx = e.x - midx
            val dy = e.y - midy
            downdeg = (Math.atan2(dy.toDouble(), dx.toDouble()) * 180 / Math.PI).toFloat()
            downdeg -= 90f
            if (downdeg < 0) {
                downdeg += 360f
            }
            downdeg = Math.floor((downdeg / 360 * (this.max + 5)).toDouble()).toFloat()

            if (crollerChangeListener != null) {
                crollerChangeListener?.onStartTrackingTouch(this)
                startEventSent = true
            }

            return true
        }
        if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - midx
            val dy = e.y - midy
            currdeg = (Math.atan2(dy.toDouble(), dx.toDouble()) * 180 / Math.PI).toFloat()
            currdeg -= 90f
            if (currdeg < 0) {
                currdeg += 360f
            }
            currdeg = Math.floor((currdeg / 360 * (this.max + 5)).toDouble()).toFloat()

            if (currdeg / (this.max + 4) > 0.75f && (downdeg - 0) / (this.max + 4) < 0.25f) {
                if (this.isAntiClockwise) {
                    deg++
                    if (deg > this.max + 2) {
                        deg = (this.max + 2).toFloat()
                    }
                } else {
                    deg--
                    if (deg < this.min + 2) {
                        deg = (this.min + 2).toFloat()
                    }
                }
            } else if (downdeg / (this.max + 4) > 0.75f && (currdeg - 0) / (this.max + 4) < 0.25f) {
                if (this.isAntiClockwise) {
                    deg--
                    if (deg < this.min + 2) {
                        deg = (this.min + 2).toFloat()
                    }
                } else {
                    deg++
                    if (deg > this.max + 2) {
                        deg = (this.max + 2).toFloat()
                    }
                }
            } else {
                if (this.isAntiClockwise) {
                    deg -= currdeg - downdeg
                } else {
                    deg += currdeg - downdeg
                }
                if (deg > this.max + 2) {
                    deg = (this.max + 2).toFloat()
                }
                if (deg < this.min + 2) {
                    deg = (this.min + 2).toFloat()
                }
            }

            downdeg = currdeg

            invalidate()
            return true
        }
        if (e.action == MotionEvent.ACTION_UP) {
            if (crollerChangeListener != null) {
                crollerChangeListener?.onStopTrackingTouch(this)
                startEventSent = false
            }
            return true
        }
        return super.onTouchEvent(e)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (parent != null && event.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        this.isEnabled = enabled
        invalidate()
    }

    private fun setlabelDisabledColor(labelDisabledColor: Int) {
        this.labelDisabledColor = labelDisabledColor
        invalidate()
    }

    private fun setSweepAngle(sweepAngle: Int) {
        this.sweepAngle = sweepAngle
        invalidate()
    }

    private fun setBackCircleRadius(backCircleRadius: Float) {
        this.backCircleRadius = backCircleRadius
        invalidate()
    }

    private fun setProgressRadius(progressRadius: Float) {
        this.progressRadius = progressRadius
        invalidate()
    }

    internal fun setOnProgressChangedListener(newListener: OnProgressChangedListener) {
        this.progressChangeListener = newListener
    }
}
