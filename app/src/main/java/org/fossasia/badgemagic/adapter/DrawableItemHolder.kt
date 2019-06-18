package org.fossasia.badgemagic.adapter

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.DrawableInfo
import org.fossasia.badgemagic.util.ImageUtils

class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card: LinearLayout = itemView.findViewById(R.id.card)
    private val image: ImageView = itemView.findViewById(R.id.image)
    var listener: OnDrawableSelected? = null

    fun bind(drawableInfo: DrawableInfo) {

        if (drawableInfo.image is VectorDrawable)
            image.setImageBitmap(ImageUtils.trim(ImageUtils.vectorToBitmap(drawableInfo.image), 80))
        else if (drawableInfo.image is BitmapDrawable)
            image.setImageBitmap(ImageUtils.trim(drawableInfo.image.bitmap, 80))

        image.setColorFilter(itemView.context.resources.getColor(android.R.color.black))

        card.setOnClickListener {
            listener?.onSelected(drawableInfo)
        }
    }
}