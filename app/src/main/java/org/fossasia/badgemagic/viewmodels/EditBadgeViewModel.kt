package org.fossasia.badgemagic.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.draw_layout.DrawMode
import org.fossasia.badgemagic.database.StorageFilesService

class EditBadgeViewModel(
    private val storageFilesService: StorageFilesService
) : ViewModel() {
    var drawModeState: ObservableField<DrawMode> = ObservableField(DrawMode.NOTHING)
    var drawingJSON: ObservableField<String> = ObservableField("[]")

    var drawState: ObservableBoolean = ObservableBoolean(true)
    var eraseState: ObservableBoolean = ObservableBoolean(false)

    var savedButton: MutableLiveData<Boolean> = MutableLiveData()
    var resetButton: MutableLiveData<Boolean> = MutableLiveData()

    init {
        savedButton.value = false
        resetButton.value = false
    }

    fun changeDrawState() {
        drawState.set(!drawState.get())
        eraseState.set(false)
        drawModeState.set(if (drawState.get()) DrawMode.DRAW else DrawMode.NOTHING)
    }

    fun changeEraseState() {
        eraseState.set(!eraseState.get())
        drawState.set(false)
        drawModeState.set(if (eraseState.get()) DrawMode.ERASE else DrawMode.NOTHING)
    }

    fun saveBadge() {
        savedButton.value = true
    }

    fun changeResetState() {
        resetButton.value = true
    }

    fun updateFiles() = storageFilesService.update()
}