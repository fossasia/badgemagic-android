package org.fossasia.badgemagic.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.DrawableInfo
import org.fossasia.badgemagic.ui.DrawerActivity

class DrawableAdapter : RecyclerView.Adapter<DrawableItemHolder>() {
    var onDrawableSelected: OnDrawableSelected? = null
    private val drawableList = mutableListOf<DrawableInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawableItemHolder {
        return DrawableItemHolder(
            when (viewType) {
                R.layout.recycler_item -> LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
                else -> LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_add, parent, false)
            }
        )
    }

    override fun getItemViewType(position: Int) = if (position == drawableList.size) R.layout.recycler_item_add else R.layout.recycler_item

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: DrawableItemHolder, position: Int) {
        if (position != drawableList.size)
            holder.apply {
                bind(drawableList[position])
                listener = onDrawableSelected
            }
        else
            holder.itemView.setOnClickListener {
                val contextAct = it.context
                if (contextAct is DrawerActivity)
                    contextAct.switchToDrawLayout()
            }
    }

    fun addAll(newDrawableList: List<DrawableInfo>) {
        if (drawableList.isNotEmpty()) drawableList.clear()
        drawableList.addAll(newDrawableList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = drawableList.size + 1
}

interface OnDrawableSelected {
    fun onSelected(selectedItem: DrawableInfo)
}