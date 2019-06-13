package org.fossasia.badgemagic.di

import org.fossasia.badgemagic.database.ClipArtService
import org.fossasia.badgemagic.database.StorageFilesService
import org.fossasia.badgemagic.viewmodels.FilesViewModel
import org.fossasia.badgemagic.viewmodels.TextArtViewModel
import org.fossasia.badgemagic.viewmodels.DrawViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { ClipArtService() }
    single { StorageFilesService() }

    viewModel { TextArtViewModel(get(), get()) }
    viewModel { FilesViewModel(get()) }
    viewModel { DrawViewModel() }
}