package org.fossasia.badgemagic.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
                card.background = ContextCompat.getDrawable(itemView.context, R.color.colorAccent)
                title.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                image.setColorFilter(ContextCompat.getColor(itemView.context, android.R.color.white))
            }
            false -> {
                card.background = ContextCompat.getDrawable(itemView.context, android.R.color.transparent)
                title.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                image.setColorFilter(ContextCompat.getColor(itemView.context, android.R.color.black))
            }
        }

        card.setOnClickListener {
            listener?.onSelected(itemPosition)
        }
    }
}
