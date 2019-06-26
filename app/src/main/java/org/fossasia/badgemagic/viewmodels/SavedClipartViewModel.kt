package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.adapter.SavedClipartsAdapter
import org.fossasia.badgemagic.data.SavedClipart
import org.fossasia.badgemagic.database.ClipArtService
import org.fossasia.badgemagic.util.ImageUtils

class SavedClipartViewModel(
    clipArtService: ClipArtService
) : ViewModel() {
    var cliparts = listOf<SavedClipart>()
    var adapter = SavedClipartsAdapter(cliparts)

    init {
        cliparts = clipArtService.getClipsFromStorage().map { SavedClipart(it.key, ImageUtils.convertToBitmap(it.value)) }
        adapter = SavedClipartsAdapter(cliparts)
    }
}
