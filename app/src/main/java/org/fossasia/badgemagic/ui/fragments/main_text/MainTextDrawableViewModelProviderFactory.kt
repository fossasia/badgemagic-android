package org.fossasia.badgemagic.ui.fragments.main_text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fossasia.badgemagic.data.fragments.FilesRepository

class MainTextDrawableViewModelProviderFactory(private val configRepository: FilesRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainTextDrawableViewModel(configRepository) as T
    }
}