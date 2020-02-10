package org.fossasia.badgemagic.adapter

import android.graphics.Bitmap
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding

class SavedClipartsViewHolder(private val binding: RecyclerItemSavedClipartBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(bitmap: Bitmap) {
        binding.bitmap = bitmap
    }

    private fun getBindingView() = binding.root
    fun getDeleteButton(): AppCompatImageView = getBindingView().findViewById(R.id.button_delete)
    fun getEditButton(): AppCompatImageView = getBindingView().findViewById(R.id.button_edit)
}
