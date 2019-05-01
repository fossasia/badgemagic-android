package com.nilhcem.blenamebadge.ui.main

import androidx.lifecycle.ViewModel
import com.nilhcem.blenamebadge.data.badge_preview.PreviewRepository
import com.nilhcem.blenamebadge.data.fragments.FilesRepository

class MainActivityViewModel(private val previewRepo: PreviewRepository, private val filesRepo: FilesRepository)
    : ViewModel() {

    fun getPreviewDetails() = previewRepo.getPreviewDetails()

    fun updateList() = filesRepo.update()
}