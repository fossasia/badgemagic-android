package org.fossasia.badgemagic.util

import org.fossasia.badgemagic.data.fragments.FilesContainer
import org.fossasia.badgemagic.data.fragments.FilesRepository
import org.fossasia.badgemagic.ui.AppViewModelProviderFactory
import org.fossasia.badgemagic.ui.fragments.main_text.MainTextDrawableViewModelProviderFactory

object InjectorUtils {

    private val filesRepo = FilesRepository.getInstance(FilesContainer.getInstance().filesData)

    fun provideFilesViewModelFactory(): AppViewModelProviderFactory {
        return AppViewModelProviderFactory(filesRepo)
    }

    fun provideMainTextDrawableViewModelFactory(): MainTextDrawableViewModelProviderFactory {
        return MainTextDrawableViewModelProviderFactory(filesRepo)
    }
}