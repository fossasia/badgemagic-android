package com.nilhcem.blenamebadge.util

import com.nilhcem.blenamebadge.data.badge_preview.PreviewContainer
import com.nilhcem.blenamebadge.data.badge_preview.PreviewRepository
import com.nilhcem.blenamebadge.data.fragments.FilesContainer
import com.nilhcem.blenamebadge.data.fragments.FilesRepository
import com.nilhcem.blenamebadge.ui.fragments.FragmentViewModelProviderFactory
import com.nilhcem.blenamebadge.ui.main.MainViewModelProviderFactory

object InjectorUtils {

    private val filesRepo = FilesRepository.getInstance(FilesContainer.getInstance().filesData)
    private val previewRepo = PreviewRepository.getInstance(PreviewContainer.getInstance().previewData)

    fun provideFilesViewModelFactory(): FragmentViewModelProviderFactory {
        return FragmentViewModelProviderFactory(filesRepo, previewRepo)
    }

    fun provideDataViewModelFactory(): MainViewModelProviderFactory {
        return MainViewModelProviderFactory(previewRepo, filesRepo)
    }
}