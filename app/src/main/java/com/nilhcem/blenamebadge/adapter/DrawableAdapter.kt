package com.nilhcem.blenamebadge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.data.DrawableInfo

class DrawableAdapter(private val context: Context, private val list: List<DrawableInfo>) : RecyclerView.Adapter<DrawableAdapter.DrawableItemHolder>() {
    private var selectedPosition: Int = -1
    private var listener: OnDrawableSelected? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawableItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return DrawableItemHolder(v)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: DrawableItemHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getSelectedItem(): DrawableInfo? {
        return if (selectedPosition == -1) null else list[selectedPosition]
    }

    fun setListener(listener: OnDrawableSelected) {
        this.listener = listener
    }

    override fun getItemCount() = list.size

    inner class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card: LinearLayout = itemView.findViewById(R.id.card)
        private val image: ImageView = itemView.findViewById(R.id.image)

        init {
            card.setOnClickListener {
                changeCardBackgrounds()
                listener?.onSelected(getSelectedItem())
            }
        }

        fun bind(drawableInfo: DrawableInfo) {
            image.setImageDrawable(drawableInfo.image)

            card.background = when {
                selectedPosition != -1 && selectedPosition == adapterPosition -> context.resources.getDrawable(R.color.colorAccent)
                else -> context.resources.getDrawable(android.R.color.transparent)
            }
        }

        private fun changeCardBackgrounds() {
            val lastSelected = selectedPosition

            selectedPosition = when {
                selectedPosition == -1 -> adapterPosition
                selectedPosition != adapterPosition -> adapterPosition
                else -> -1
            }

            notifyItemChanged(adapterPosition)
            if (lastSelected != -1) notifyItemChanged(lastSelected)
        }
    }
}

interface OnDrawableSelected {
    fun onSelected(selectedItem: DrawableInfo?)
}