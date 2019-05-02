package com.nilhcem.blenamebadge.adapter

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.data.ModeInfo
import pl.droidsonroids.gif.GifDrawable

class ModeAdapter(private val context: Context?, private val list: List<ModeInfo>) : RecyclerView.Adapter<ModeAdapter.DrawableItemHolder>() {
    private var selectedPosition: Int = 0
    private var listener: OnModeSelected? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawableItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recycler_item_mode, parent, false)
        return DrawableItemHolder(v)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: DrawableItemHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setListener(listener: OnModeSelected) {
        this.listener = listener
    }

    override fun getItemCount() = list.size

    fun getSelectedItemPosition(): Int = selectedPosition

    inner class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card: LinearLayout = itemView.findViewById(R.id.card)
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title_tile)

        init {
            card.setOnClickListener {
                changeCardBackgrounds()
                listener?.onSelected()
            }
        }

        fun bind(ModeInfo: ModeInfo) {
            if (context != null)
                image.setImageDrawable(GifDrawable(context.resources, ModeInfo.drawableID))

            title.text = ModeInfo.mode.toString()

            card.background = when (selectedPosition) {
                adapterPosition -> context?.resources?.getDrawable(R.color.colorAccent)
                else -> context?.resources?.getDrawable(android.R.color.transparent)
            }
            title.setTextColor(when (selectedPosition) {
                adapterPosition -> context?.resources?.getColor(android.R.color.white)
                    ?: Color.parseColor("#000000")
                else -> context?.resources?.getColor(android.R.color.black)
                    ?: Color.parseColor("#00000000")
            })
            image.setColorFilter(when (selectedPosition) {
                adapterPosition -> context?.resources?.getColor(android.R.color.white)
                    ?: Color.parseColor("#000000")
                else -> context?.resources?.getColor(android.R.color.black)
                    ?: Color.parseColor("#00000000")
            })
        }

        private fun changeCardBackgrounds() {
            val lastSelected = selectedPosition

            selectedPosition = when {
                selectedPosition != adapterPosition -> adapterPosition
                else -> selectedPosition
            }

            notifyItemChanged(adapterPosition)
            if (lastSelected != -1) notifyItemChanged(lastSelected)
        }
    }
}

interface OnModeSelected {
    fun onSelected()
}