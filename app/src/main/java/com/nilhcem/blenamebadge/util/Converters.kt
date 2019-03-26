package com.nilhcem.blenamebadge.util

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.DrawableRes
import java.math.BigInteger
import java.util.ArrayList

object Converters {
    fun convertImageToLEDHex(context: Context, @DrawableRes dId: Int): List<String> {
        val myIcon = context.resources.getDrawable(dId)
        val bm = (myIcon as BitmapDrawable).bitmap

        val height = bm.height
        val width = bm.width
        val blackValue = -16777216

        val image = Array(height) { IntArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                image[i][j] = if (bm.getPixel(j, i) == blackValue) 1 else 0
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

        val rOff = Math.floor((diff.toFloat() / 2).toDouble()).toInt()
        val lOff = Math.ceil((diff.toFloat() / 2).toDouble()).toInt()

        val list = ArrayList<ArrayList<Int>>()
        for (i in 0 until height) {
            val row = ArrayList<Int>()
            for (j in 0 until rOff) {
                row.add(0)
            }
            list.add(row)
        }
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (image[i][j] != -1)
                    list[i].add(image[i][j])
            }
        }
        for (i in 0 until height) {
            for (j in 0 until lOff) {
                list[i].add(0)
            }
        }
        val allHexs = ArrayList<String>()
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
}
