package org.fossasia.badgemagic.ui.fragments.main_textart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fossasia.badgemagic.data.clipart.ClipArtRepository
import org.fossasia.badgemagic.data.saved_files.FilesRepository

class MainTextArtViewModelProviderFactory(private val configRepository: FilesRepository, private val clipArtRepository: ClipArtRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainTextArtViewModel(configRepository, clipArtRepository) as T
    }
}