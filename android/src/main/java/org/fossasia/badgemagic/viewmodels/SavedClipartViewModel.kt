package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.adapter.SavedClipartsAdapter
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.database.ClipArtService

class SavedClipartViewModel(
    private val clipArtService: ClipArtService
) : ViewModel() {

    var cliparts = listOf<SavedClipart>()
    var adapter = SavedClipartsAdapter(cliparts, this)

    fun getStorageClipartLiveData() = clipArtService.getClipsFromStorage()

    fun deleteClipart(position: Int) {
        if (cliparts.isNotEmpty() && position < cliparts.size)
            clipArtService.deleteClipart(cliparts[position].fileName)
    }

    fun setList(list: List<SavedClipart>) {
        cliparts = list
    }

    fun isEmpty() = cliparts.isEmpty()
}
