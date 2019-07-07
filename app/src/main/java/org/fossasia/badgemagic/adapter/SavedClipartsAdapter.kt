package org.fossasia.badgemagic.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding
import org.fossasia.badgemagic.viewmodels.SavedClipartViewModel

class SavedClipartsAdapter(
    private var clipartList: List<Bitmap>,
    private val viewModel: SavedClipartViewModel
) : RecyclerView.Adapter<SavedClipartsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedClipartsViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerItemSavedClipartBinding>(LayoutInflater.from(parent.context), R.layout.recycler_item_saved_clipart, parent, false)
        return SavedClipartsViewHolder(binding)
    }

    override fun getItemCount(): Int = clipartList.size

    override fun onBindViewHolder(holder: SavedClipartsViewHolder, position: Int) {
        holder.bind(clipartList[position])
        holder.getBindingView().setOnClickListener {
            viewModel.deleteClipart(position)
            Toast.makeText(it.context, "Delete Clipart Successfully", Toast.LENGTH_LONG).show()
        }
    }

    fun setList(list: List<Bitmap>) {
        clipartList = list
        notifyDataSetChanged()
    }
}
