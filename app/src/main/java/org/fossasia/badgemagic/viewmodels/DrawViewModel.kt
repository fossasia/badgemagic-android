package org.fossasia.badgemagic.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.draw_layout.DrawMode

class DrawViewModel : ViewModel() {
    var drawModeState: ObservableField<DrawMode> = ObservableField(DrawMode.NOTHING)

    var drawState: ObservableBoolean = ObservableBoolean(false)
    var eraseState: ObservableBoolean = ObservableBoolean(false)
    var resetState: ObservableBoolean = ObservableBoolean(false)

    fun changeDrawState() {
        drawState.set(!drawState.get())
        drawModeState.set(if (drawState.get()) DrawMode.DRAW else DrawMode.NOTHING)
    }

    fun changeEraseState() {
        eraseState.set(!eraseState.get())
        drawModeState.set(if (eraseState.get()) DrawMode.ERASE else DrawMode.NOTHING)
    }

    fun changeResetState() {
        resetState.set(!resetState.get())
    }
}