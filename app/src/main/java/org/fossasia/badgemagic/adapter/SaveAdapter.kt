package org.fossasia.badgemagic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.fragments.BadgeConfig
import org.fossasia.badgemagic.data.fragments.ConfigInfo
import org.fossasia.badgemagic.util.MoshiUtils

class SaveAdapter(private val context: Context?, private val list: List<ConfigInfo>, private val listener: OnSavedItemSelected) : RecyclerView.Adapter<SaveAdapter.SaveItemHolder>() {
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recycler_save_item, parent, false)
        return SaveItemHolder(v)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: SaveItemHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun resetSelectedItem() {
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun getSelectedItem(): ConfigInfo? {
        return if (selectedPosition == -1) null else list[selectedPosition]
    }

    inner class SaveItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card: LinearLayout = itemView.findViewById(R.id.card)
        private val text: TextView = itemView.findViewById(R.id.text)
        private val options: AppCompatImageView = itemView.findViewById(R.id.options)
        private val playPause: AppCompatImageView = itemView.findViewById(R.id.play_pause)
        private val editButton: AppCompatImageView = itemView.findViewById(R.id.button_edit)
        private val chipFlash: Chip = itemView.findViewById(R.id.chip_flash)
        private val chipMarquee: Chip = itemView.findViewById(R.id.chip_marquee)
        private val chipInverted: Chip = itemView.findViewById(R.id.chip_inverted)
        private val chipSpeed: Chip = itemView.findViewById(R.id.chip_speed)
        private val chipMode: Chip = itemView.findViewById(R.id.chip_mode)

        init {
            playPause.setOnClickListener {
                changeCardBackgrounds()
                listener.onSelected(if (selectedPosition == -1) null else list[selectedPosition])
            }
            options.setOnClickListener {
                listener.onOptionsSelected(list[adapterPosition])
            }
            editButton.setOnClickListener {
                listener.onEdit(list[adapterPosition])
            }
        }

        fun bind(item: ConfigInfo) {
            text.text = item.fileName.substring(0, item.fileName.lastIndexOf("."))

            card.background = when {
                selectedPosition != -1 && selectedPosition == adapterPosition -> ContextCompat.getDrawable(itemView.context, R.color.colorAccent)
                else -> ContextCompat.getDrawable(itemView.context, android.R.color.transparent)
            }
            text.setTextColor(
                    when {
                        selectedPosition != -1 && selectedPosition == adapterPosition -> ContextCompat.getColor(itemView.context, android.R.color.white)
                        else -> ContextCompat.getColor(itemView.context, android.R.color.black)
                    }
            )
            playPause.setColorFilter(
                    when {
                        selectedPosition != -1 && selectedPosition == adapterPosition -> ContextCompat.getColor(itemView.context, android.R.color.white)
                        else -> ContextCompat.getColor(itemView.context, android.R.color.black)
                    }
            )
            editButton.setColorFilter(
                    when {
                        selectedPosition != -1 && selectedPosition == adapterPosition -> ContextCompat.getColor(itemView.context, android.R.color.white)
                        else -> ContextCompat.getColor(itemView.context, android.R.color.black)
                    }
            )
            options.setColorFilter(
                    when {
                        selectedPosition != -1 && selectedPosition == adapterPosition -> ContextCompat.getColor(itemView.context, android.R.color.white)
                        else -> ContextCompat.getColor(itemView.context, android.R.color.black)
                    }
            )

            val badge: BadgeConfig? = MoshiUtils.getAdapter().fromJson(item.badgeJSON)
            chipSpeed.text = (badge?.speed?.ordinal?.plus(1)).toString()
            chipMode.text = badge?.mode?.toString()

            chipFlash.visibility = if (badge?.isFlash == true) View.VISIBLE else View.GONE
            chipMarquee.visibility = if (badge?.isMarquee == true) View.VISIBLE else View.GONE
            chipInverted.visibility = if (badge?.isInverted == true) View.VISIBLE else View.GONE
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

interface OnSavedItemSelected {
    fun onSelected(item: ConfigInfo?)
    fun onEdit(item: ConfigInfo?)
    fun onOptionsSelected(item: ConfigInfo)
}
