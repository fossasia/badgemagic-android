package org.fossasia.badgemagic.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.ModeInfo
import pl.droidsonroids.gif.GifDrawable

class ModeItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card: LinearLayout = itemView.findViewById(R.id.card)
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title_tile)
    var listener: OnModeSelected? = null

    fun bind(ModeInfo: ModeInfo, modeSelectedPosition: Int, itemPosition: Int) {
        val resources = itemView.context.resources
        if (resources != null)
            image.setImageDrawable(GifDrawable(resources, ModeInfo.drawableID))

        title.text = ModeInfo.mode.toString().toLowerCase().capitalize()

        when (itemPosition == modeSelectedPosition) {
            true -> {
                card.background = itemView.context.resources.getDrawable(R.color.colorAccent)
                title.setTextColor(itemView.context.resources.getColor(android.R.color.white))
                image.setColorFilter(itemView.context.resources.getColor(android.R.color.white))
            }
            false -> {
                card.background = itemView.context.resources.getDrawable(android.R.color.transparent)
                title.setTextColor(itemView.context.resources.getColor(android.R.color.black))
                image.setColorFilter(itemView.context.resources.getColor(android.R.color.black))
            }
        }

        card.setOnClickListener {
            listener?.onSelected(itemPosition)
        }
    }
}
