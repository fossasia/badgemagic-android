package com.nilhcem.blenamebadge.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.data.ModeInfo
import com.nilhcem.blenamebadge.util.Resource
import pl.droidsonroids.gif.GifDrawable

class ModeItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val card: LinearLayout = itemView.findViewById(R.id.card)
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title_tile)
    private val resourceHelper = Resource()
    var listener: OnModeSelected? = null

    fun bind(ModeInfo: ModeInfo, modeSelectedPosition: Int, itemPosition: Int) {
        val resources = resourceHelper.getResources()
        if (resources != null)
            image.setImageDrawable(GifDrawable(resources, ModeInfo.drawableID))

        title.text = ModeInfo.mode.toString().toLowerCase().capitalize()

        when (itemPosition == modeSelectedPosition) {
            true -> {
                card.background = resourceHelper.getDrawable(R.color.colorAccent)
                title.setTextColor(resourceHelper.getColor(android.R.color.white)
                    ?: Color.parseColor("#000000"))
                image.setColorFilter(resourceHelper.getColor(android.R.color.white)
                    ?: Color.parseColor("#000000"))
            }
            false -> {
                card.background = resourceHelper.getDrawable(android.R.color.transparent)
                title.setTextColor(resourceHelper.getColor(android.R.color.black)
                    ?: Color.parseColor("#00000000"))
                image.setColorFilter(resourceHelper.getColor(android.R.color.black)
                    ?: Color.parseColor("#00000000"))
            }
        }

        card.setOnClickListener {
            listener?.onSelected(itemPosition)
        }
    }
}