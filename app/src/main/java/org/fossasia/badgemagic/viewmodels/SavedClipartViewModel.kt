package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.adapter.SavedClipartsAdapter
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.database.ClipArtService
import org.fossasia.badgemagic.util.ImageUtils
import org.fossasia.badgemagic.util.StorageUtils

class SavedClipartViewModel(
    private val clipArtService: ClipArtService
) : ViewModel() {

    var cliparts = listOf<SavedClipart>()
    var adapter = SavedClipartsAdapter(cliparts.map { it.bitmap }, this)

    init {
        cliparts = clipArtService.getClipsFromStorage().map { SavedClipart(it.key, ImageUtils.convertToBitmap(it.value)) }
        adapter = SavedClipartsAdapter(cliparts.map { it.bitmap }, this)
    }

    private fun update() {
        clipArtService.updateClipArts()
        cliparts = clipArtService.getClipsFromStorage().map { SavedClipart(it.key, ImageUtils.convertToBitmap(it.value)) }
        adapter.setList(cliparts.map { it.bitmap })
    }

    fun deleteClipart(position: Int) {
        StorageUtils.deleteClipart(cliparts[position].fileName)
        update()
    }
}
