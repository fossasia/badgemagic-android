package org.fossasia.badgemagic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding

class SavedClipartsAdapter(
    private val clipartList: List<SavedClipart>
) : RecyclerView.Adapter<SavedClipartsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedClipartsViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerItemSavedClipartBinding>(LayoutInflater.from(parent.context), R.layout.recycler_item_saved_clipart, parent, false)
        return SavedClipartsViewHolder(binding)
    }

    override fun getItemCount(): Int = clipartList.size

    override fun onBindViewHolder(holder: SavedClipartsViewHolder, position: Int) {
        holder.bind(clipartList[position])
    }
}
