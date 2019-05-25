package org.fossasia.badgemagic.adapter

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.DrawableInfo
import org.fossasia.badgemagic.util.ImageUtils
import org.fossasia.badgemagic.util.Resource

class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card: LinearLayout = itemView.findViewById(R.id.card)
    private val image: ImageView = itemView.findViewById(R.id.image)
    var listener: OnDrawableSelected? = null
    private val resource = Resource()

    fun bind(drawableInfo: DrawableInfo) {
        image.setImageBitmap(ImageUtils.trim((drawableInfo.image as BitmapDrawable).bitmap, 100))

        image.setColorFilter(resource.getColor(android.R.color.black)
            ?: Color.parseColor("#000000"))

        card.setOnClickListener {
            listener?.onSelected(drawableInfo)
        }
    }
}