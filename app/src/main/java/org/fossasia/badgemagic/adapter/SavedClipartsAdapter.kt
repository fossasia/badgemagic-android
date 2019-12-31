package org.fossasia.badgemagic.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.databinding.RecyclerItemSavedClipartBinding
import org.fossasia.badgemagic.ui.EditClipartActivity
import org.fossasia.badgemagic.viewmodels.SavedClipartViewModel

class SavedClipartsAdapter(
    private var clipartList: List<SavedClipart>,
    private val viewModel: SavedClipartViewModel
) : RecyclerView.Adapter<SavedClipartsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedClipartsViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerItemSavedClipartBinding>(LayoutInflater.from(parent.context), R.layout.recycler_item_saved_clipart, parent, false)
        return SavedClipartsViewHolder(binding)
    }

    override fun getItemCount(): Int = clipartList.size

    override fun onBindViewHolder(holder: SavedClipartsViewHolder, position: Int) {
        holder.bind(clipartList[position].bitmap)
        holder.getDeleteButton().setOnClickListener {
            deleteWarning(it, position)
        }
        holder.getEditButton().setOnClickListener {
            it.context.startActivity(
                Intent(it.context, EditClipartActivity::class.java).apply {
                    putExtra("fileName", clipartList[position].fileName)
                }
            )
        }
    }

    private fun deleteWarning(it: View, position: Int) {
        val dialogMessage = it.resources.getString(R.string.clipart_delete_warning)
        val builder = android.app.AlertDialog.Builder(it.context)
        builder.setIcon(R.drawable.ic_delete_black_24dp)
        builder.setTitle(it.resources.getString(R.string.delete))
        builder.setMessage(dialogMessage)
        builder.setPositiveButton("OK") { _, _ ->
            viewModel.deleteClipart(position)
            Toast.makeText(it.context, it.resources.getString(R.string.delete_clipart_confirm), Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
        }
        builder.create().show()
    }

    fun setList(list: List<SavedClipart>) {
        clipartList = list
        viewModel.setList(list)
        notifyDataSetChanged()
    }
}
