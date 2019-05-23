package org.fossasia.badgemagic.util

import org.fossasia.badgemagic.data.clipart.ClipArtContainer
import org.fossasia.badgemagic.data.clipart.ClipArtRepository
import org.fossasia.badgemagic.data.saved_files.FilesContainer
import org.fossasia.badgemagic.data.saved_files.FilesRepository
import org.fossasia.badgemagic.ui.AppViewModelProviderFactory
import org.fossasia.badgemagic.ui.fragments.main_textart.MainTextArtViewModelProviderFactory

object InjectorUtils {

    private val filesRepo = FilesRepository.getInstance(FilesContainer.getInstance().filesData)
    private val clipArtRepo = ClipArtRepository.getInstance(ClipArtContainer.getInstance().clipArts)

    fun provideFilesViewModelFactory(): AppViewModelProviderFactory {
        return AppViewModelProviderFactory(filesRepo)
    }

    fun provideMainTextDrawableViewModelFactory(): MainTextArtViewModelProviderFactory {
        return MainTextArtViewModelProviderFactory(filesRepo, clipArtRepo)
    }
}