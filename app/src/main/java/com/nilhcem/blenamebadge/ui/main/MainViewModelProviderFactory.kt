package com.nilhcem.blenamebadge.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nilhcem.blenamebadge.data.badge_preview.PreviewRepository
import com.nilhcem.blenamebadge.data.fragments.FilesRepository

class MainViewModelProviderFactory(private val previewRepo: PreviewRepository, private val filesRepo: FilesRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(previewRepo, filesRepo) as T
    }
}
