package com.nilhcem.blenamebadge.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.data.DrawableInfo
import com.nilhcem.blenamebadge.util.Resource

class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card: LinearLayout = itemView.findViewById(R.id.card)
    private val image: ImageView = itemView.findViewById(R.id.image)
    var listener: OnDrawableSelected? = null
    private val resource = Resource()

    fun bind(drawableInfo: DrawableInfo, drawableSelectedPosition: Int, itemPosition: Int) {
        image.setImageBitmap(trim((drawableInfo.image as BitmapDrawable).bitmap))
        card.background = if (drawableSelectedPosition == itemPosition)
            resource.getDrawable(R.color.colorAccent) else resource.getDrawable(android.R.color.transparent)
        image.setColorFilter((if (drawableSelectedPosition == itemPosition)
            resource.getColor(android.R.color.white) else resource.getColor(android.R.color.black))
            ?: Color.parseColor("#000000"))

        card.setOnClickListener {
            listener?.onSelected(itemPosition)
        }
    }

    private fun trim(source: Bitmap): Bitmap {
        var firstX = 0
        var firstY = 0
        var lastX = source.width
        var lastY = source.height
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)
        loop@ for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                    firstX = when {
                        x > 1 -> x - 1
                        else -> x
                    }
                    break@loop
                }
            }
        }
        loop@ for (y in 0 until source.height) {
            for (x in firstX until source.width) {
                if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                    firstY = when {
                        y > 1 -> y - 1
                        else -> y
                    }
                    break@loop
                }
            }
        }
        loop@ for (x in source.width - 1 downTo firstX) {
            for (y in source.height - 1 downTo firstY) {
                if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                    lastX = when {
                        x < source.width - 2 -> x + 2
                        else -> x + 1
                    }
                    break@loop
                }
            }
        }
        loop@ for (y in source.height - 1 downTo firstY) {
            for (x in source.width - 1 downTo firstX) {
                if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                    lastY = when {
                        y < source.height - 2 -> y + 2
                        else -> y + 1
                    }
                    break@loop
                }
            }
        }

        val trimmedBitmap = Bitmap.createBitmap(source, firstX, firstY, lastX - firstX, lastY - firstY)

        val maxSize = 100
        val outWidth: Int
        val outHeight: Int
        val inWidth = trimmedBitmap.width
        val inHeight = trimmedBitmap.height
        if (inWidth > inHeight) {
            outWidth = maxSize
            outHeight = inHeight * maxSize / inWidth
        } else {
            outHeight = maxSize
            outWidth = inWidth * maxSize / inHeight
        }
        return Bitmap.createScaledBitmap(trimmedBitmap, outWidth, outHeight, false)
    }
}