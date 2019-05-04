package com.nilhcem.blenamebadge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nilhcem.blenamebadge.data.fragments.FilesRepository

class AppViewModelProviderFactory(private val configRepository: FilesRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(configRepository) as T
    }
}
