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
    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawableItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return DrawableItemHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: DrawableItemHolder, position: Int) {
        val current = list[position]
        holder.image.setImageDrawable(current.image)
        holder.card.setOnClickListener {
            selectedPosition = if (selectedPosition == -1) position else if (selectedPosition != position) position else -1
            notifyDataSetChanged()
        }

        if (selectedPosition != -1 && selectedPosition == position) {
            holder.card.background = context.resources.getDrawable(R.color.colorAccent)
        } else {
            holder.card.background = context.resources.getDrawable(android.R.color.transparent)
        }
    }

    fun getSelectedItem(): DrawableInfo? {
        return if (selectedPosition == -1) null else list[selectedPosition]
    }    

    override fun getItemCount(): Int {
        return list.size
    }

    inner class DrawableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var card: LinearLayout = itemView.findViewById(R.id.card)
        var image: ImageView = itemView.findViewById(R.id.image)
    }
}
