package org.fossasia.badgemagic.adapter

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding

class SavedClipartsViewHolder(private val binding: RecyclerItemSavedClipartBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(bitmap: Bitmap) {
        binding.bitmap = bitmap
    }

    fun getBindingView() = binding.root
}
