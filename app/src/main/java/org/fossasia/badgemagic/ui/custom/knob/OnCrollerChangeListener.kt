package org.fossasia.badgemagic.ui.custom.knob

interface OnCrollerChangeListener {
    fun onProgressChanged(croller: Croller, progress: Int)

    fun onStartTrackingTouch(croller: Croller)

    fun onStopTrackingTouch(croller: Croller)
}
