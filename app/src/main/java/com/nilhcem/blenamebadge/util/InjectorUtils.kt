package com.nilhcem.blenamebadge.util

import com.nilhcem.blenamebadge.data.fragments.FilesContainer
import com.nilhcem.blenamebadge.data.fragments.FilesRepository
import com.nilhcem.blenamebadge.ui.fragments.FragmentViewModelProviderFactory

object InjectorUtils {

    private val filesRepo = FilesRepository.getInstance(FilesContainer.getInstance().filesData)

    fun provideFilesViewModelFactory(): FragmentViewModelProviderFactory {
        return FragmentViewModelProviderFactory(filesRepo)
    }
}