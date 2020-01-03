package org.fossasia.badgemagic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.ModeInfo

class ModeAdapter : RecyclerView.Adapter<ModeItemHolder>() {
    private var selectedPosition: Int = 0
    var onModeSelected: OnModeSelected? = null
    private val list = mutableListOf<ModeInfo>()

    fun addAll(modeList: List<ModeInfo>) {
        if (list.isNotEmpty()) list.clear()
        list.addAll(modeList)
        notifyDataSetChanged()
    }

    fun setSelectedAnimationPosition(position: Int) {
        if (position == -1) return
        selectedPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_mode, parent, false)
        return ModeItemHolder(v)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: ModeItemHolder, position: Int) {
        holder.apply {
            bind(list[position], selectedPosition, position)
            listener = onModeSelected
        }
    }

    override fun getItemCount() = list.size

    fun getSelectedItemPosition(): Int = selectedPosition
}

interface OnModeSelected {
    fun onSelected(position: Int)
}
