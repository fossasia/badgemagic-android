package com.nilhcem.blenamebadge.util

import com.nilhcem.blenamebadge.data.fragments.FilesContainer
import com.nilhcem.blenamebadge.data.fragments.FilesRepository
import com.nilhcem.blenamebadge.ui.AppViewModelProviderFactory
import com.nilhcem.blenamebadge.ui.fragments.main_text.MainTextDrawableViewModelProviderFactory

object InjectorUtils {

    private val filesRepo = FilesRepository.getInstance(FilesContainer.getInstance().filesData)

    fun provideFilesViewModelFactory(): AppViewModelProviderFactory {
        return AppViewModelProviderFactory(filesRepo)
    }

    fun provideMainTextDrawableViewModelFactory(): MainTextDrawableViewModelProviderFactory {
        return MainTextDrawableViewModelProviderFactory(filesRepo)
    }
}