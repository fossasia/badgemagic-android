package org.fossasia.badgemagic.adapter

import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding

class SavedClipartsViewHolder(private val binding: RecyclerItemSavedClipartBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(savedClipart: SavedClipart) {
        binding.savedItem = savedClipart
    }
}
