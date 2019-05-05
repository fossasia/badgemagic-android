package com.nilhcem.blenamebadge.adapter

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
        image.setImageDrawable(drawableInfo.image)
        card.background = if (drawableSelectedPosition == itemPosition)
            resource.getDrawable(R.color.colorAccent) else resource.getDrawable(android.R.color.transparent)

        card.setOnClickListener {
            listener?.onSelected(itemPosition)
        }
    }
}