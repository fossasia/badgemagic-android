package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.database.StorageFilesService

class DrawerViewModel(
    private val storageFilesService: StorageFilesService
) : ViewModel() {
    var swappingOrientation = false

    fun updateList() = storageFilesService.update()
}
