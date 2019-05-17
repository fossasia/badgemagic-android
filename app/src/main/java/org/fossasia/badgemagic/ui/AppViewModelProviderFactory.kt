package org.fossasia.badgemagic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fossasia.badgemagic.data.fragments.FilesRepository

class AppViewModelProviderFactory(private val configRepository: FilesRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(configRepository) as T
    }
}
