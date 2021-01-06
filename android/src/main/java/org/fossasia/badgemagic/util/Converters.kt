package org.fossasia.badgemagic.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.SparseArray
import android.util.TypedValue
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor
import org.fossasia.badgemagic.data.badge_preview.CheckList
import org.fossasia.badgemagic.device.DataToByteArrayConverter

const val DRAWABLE_START = '«'
const val DRAWABLE_END = '»'

object Converters {

    private fun textAsBitmap(text: String, invertLED: Boolean): List<String> {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 11f
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.LEFT
        val baseline: Float = -paint.ascent()
        var width = (paint.measureText(text) + 0.0f).toInt()
        var height = (baseline + paint.descent() + 0.0f).toInt()
        val trueWidth = width
        if (width > height) height = width else width = height
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, width.div(2).minus(trueWidth.div(2)).toFloat(), baseline, paint)

        return convertBitmapToLEDHex(image, invertLED)
    }

    fun convertDrawableToLEDHex(drawableIcon: Drawable?, invertLED: Boolean): List<String> {
        var bm = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        if (drawableIcon is VectorDrawable)
            bm = ImageUtils.scaleBitmap(ImageUtils.vectorToBitmap(drawableIcon), 40)
        else if (drawableIcon is BitmapDrawable)
            bm = ImageUtils.scaleBitmap((drawableIcon).bitmap, 40)
        return convertBitmapToLEDHex(bm, invertLED)
    }

    fun convertBitmapToLEDHex(bm: Bitmap, invertLED: Boolean): List<String> {
        val height = bm.height
        val width = bm.width

        val image = Array(height) { IntArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                image[i][j] = if (bm.getPixel(j, i) != 0) 1 else 0
            }
        }
        var finalSum = 0
        for (j in 0 until width) {
            var sum = 0
            for (i in 0 until height) {
                sum += image[i][j]
            }
            if (sum == 0) {
                for (i in 0 until height) {
                    image[i][j] = -1
                }
            } else {
                finalSum += j
                break
            }
        }

        for (j in (width - 1) downTo 0) {
            var sum = 0
            for (i in 1 until height) {
                sum += image[i][j]
            }
            if (sum == 0) {
                for (i in 0 until height) {
                    image[i][j] = -1
                }
            } else {
                finalSum += (height) - j - 1
                break
            }
        }

        var diff = 0
        if ((height - finalSum) % 8 > 0)
            diff = 8 - (height - finalSum) % 8

        val rOff = floor((diff.toFloat() / 2).toDouble()).toInt()
        val lOff = ceil((diff.toFloat() / 2).toDouble()).toInt()

        val list: MutableList<MutableList<Int>> = mutableListOf()
        for (i in 0 until height) {
            val row = mutableListOf<Int>()
            for (j in 0 until rOff) {
                row.add(0)
            }
            list.add(row)
        }
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (image[i][j] != -1)
                    list[i].add(image[i][j]
                    )
            }
        }
        for (i in 0 until height) {
            for (j in 0 until lOff) {
                list[i].add(0)
            }
        }

        // Reformatting Against invertLED
        for (i in 0 until list.size) {
            for (j in 0 until list[0].size) {
                list[i][j] = if (list[i][j] == 1) {
                    if (!invertLED) 1 else 0
                } else {
                    if (!invertLED) 0 else 1
                }
            }
        }

        val allHexs = mutableListOf<String>()
        for (i in 0 until list[0].size / 8) {
            val lineHex = StringBuilder()
            for (k in 0 until height) {

                val stBuilder = StringBuilder()
                for (j in i * 8 until i * 8 + 8) {
                    stBuilder.append(list[k][j])
                }
                val hex = StringBuilder(BigInteger(stBuilder.toString(), 2).toString(16))
                if (hex.length == 1)
                    hex.insert(0, '0')
                lineHex.append(hex.toString())
            }
            allHexs.add(lineHex.toString())
        }
        return allHexs
    }

    fun hexToBin(s: String): String {
        val number = BigInteger(s, 16).toString(2)
        val sb = StringBuilder(number)
        for (i in 0 until 8 - number.length) {
            sb.insert(0, "0")
        }
        return sb.toString()
    }

    fun DpToPx(dip: Int, context: Context): Int {
        val r: Resources = context.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip.toFloat(),
                r.getDisplayMetrics()
        ).toInt()
    }

    private fun invertHex(hex: String): String {
        val stBuilder = StringBuilder()
        for (i in 0 until hex.length / 2) {
            val tempstBuilder = StringBuilder()
            val bin = hexToBin(hex.substring(i * 2, i * 2 + 2))
            for (char in bin)
                tempstBuilder.append(if (char == '0') '1' else '0')
            var newHex = BigInteger(tempstBuilder.toString(), 2).toString(16)
            for (j in 0 until 2 - newHex.length) {
                newHex = "0$newHex"
            }
            stBuilder.append(newHex)
        }
        return stBuilder.toString()
    }

    fun convertTextToLEDHex(data: String, invertLED: Boolean): Pair<Boolean, List<String>> {
        var valid = true
        val list = mutableListOf<String>()
        for (letter in data) {
            if (DataToByteArrayConverter.CHAR_CODES.containsKey(letter)) {
                list.add(
                        if (invertLED)
                            invertHex(DataToByteArrayConverter.CHAR_CODES.getValue(letter))
                        else
                            DataToByteArrayConverter.CHAR_CODES.getValue(letter)
                )
            } else {
                valid = false
            }
        }
        return Pair(valid, list)
    }

    fun fixLEDHex(allHex: List<String>, isInverted: Boolean): List<String> {
        if (!isInverted) {
            return allHex
        }
        val list = mutableListOf<String>()
        for (str in allHex) {
            list.add(invertHex(str))
        }
        return list
    }

    fun convertEditableToLEDHex(editable: String, invertLED: Boolean, drawableSparse: SparseArray<Drawable>): List<String> {
        val listOfArt = mutableListOf<String>()
        var i = 0
        while (i < editable.length) {
            val ch = editable[i]
            if (ch == DRAWABLE_START) {
                val foundIndex = editable.indexOf(DRAWABLE_END, i)
                i = if (foundIndex > 0) {
                    listOfArt.addAll(
                            convertDrawableToLEDHex(drawableSparse.get(editable.substring(i + 1, foundIndex).toInt()), invertLED)
                    )
                    foundIndex + 1
                } else {
                    editable.length
                }
            } else {
                val foundIndex = getIndexOfNextKnown(editable.substring(i, editable.length))
                if (foundIndex == 0) {
                    listOfArt.addAll(
                            convertTextToLEDHex(ch.toString(), invertLED).second
                    )
                    i++
                } else {
                    val targetLength = if (foundIndex > 0) i + foundIndex + 1 else editable.length
                    listOfArt.addAll(
                            textAsBitmap(editable.substring(i, targetLength), invertLED)
                    )
                    i = targetLength
                }
            }
        }
        return handleInvertLED(listOfArt, invertLED)
    }

    private fun getIndexOfNextKnown(str: String): Int {
        str.forEachIndexed { index, c ->
            if (DataToByteArrayConverter.CHAR_CODES.containsKey(c) || c == DRAWABLE_START) {
                return index
            }
        }
        return -1
    }

    private fun handleInvertLED(hexStrings: List<String>, isInverted: Boolean): List<String> {
        if (!isInverted || !checkValueInFirstColumn(hexStrings))
            return hexStrings

        val listNew = mutableListOf<String>()

        for (i in 0 until 11) {
            listNew.add("")
        }

        for (line in hexStrings) {
            for (i in line.indices step 2) {
                listNew[i / 2] += line.substring(i, i + 2)
            }
        }

        for (i in listNew.indices) {
            var binary = BigInteger(listNew[i], 16).toString(2)
            while (binary.length % 8 != 0)
                binary = "0$binary"

            listNew[i] = BigInteger("1" + binary + "0000000", 2).toString(16)
        }

        val allStrings = mutableListOf<String>()
        for (i in listNew[0].indices step 2) {
            var tempStr = ""
            for (j in 0 until 11) {
                tempStr += listNew[j].substring(i, i + 2)
            }
            allStrings.add(tempStr)
        }

        return allStrings
    }

    private fun checkValueInFirstColumn(hexStrings: List<String>): Boolean {
        if (hexStrings.isNotEmpty())
            for (i in hexStrings[0].indices step 2) {
                if (BigInteger(hexStrings[0][i].toString(), 16).toString(10).toInt() < 8)
                    return true
            }
        return false
    }

    fun convertStringsToLEDHex(list: ArrayList<CheckList>): Bitmap {
        val newBitmap = Bitmap.createBitmap(list[0].list.size, list.size, Bitmap.Config.ARGB_8888)
        for (i in 0 until list.size) {
            for (j in 0 until list[0].list.size) {
                newBitmap.setPixel(j, i,
                        if (list[i].list[j])
                            Color.BLACK
                        else
                            Color.TRANSPARENT
                )
            }
        }
        return newBitmap
    }
}
