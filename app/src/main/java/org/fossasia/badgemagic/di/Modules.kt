package org.fossasia.badgemagic.di

import org.fossasia.badgemagic.database.ClipArtService
import org.fossasia.badgemagic.database.StorageFilesService
import org.fossasia.badgemagic.util.PreferenceUtils
import org.fossasia.badgemagic.util.Resource
import org.fossasia.badgemagic.viewmodels.FilesViewModel
import org.fossasia.badgemagic.viewmodels.TextArtViewModel
import org.fossasia.badgemagic.viewmodels.SettingsViewModel
import org.fossasia.badgemagic.viewmodels.EditBadgeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { TextArtViewModel(get(), get()) }
    viewModel { FilesViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { EditBadgeViewModel(get()) }
}

val singletonModules = module {
    single { ClipArtService() }
    single { StorageFilesService() }
}

val utilModules = module {
    single { PreferenceUtils(androidContext()) }
    single { Resource(androidContext()) }
}