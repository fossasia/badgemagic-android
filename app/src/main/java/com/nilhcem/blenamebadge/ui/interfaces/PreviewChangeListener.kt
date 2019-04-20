package com.nilhcem.blenamebadge.ui.interfaces

import com.nilhcem.blenamebadge.device.model.Mode
import com.nilhcem.blenamebadge.device.model.Speed

interface PreviewChangeListener {
    fun onPreviewChange(hexStrings: List<String>, marquee: Boolean, flash: Boolean, speed: Speed, mode: Mode)
    fun updateSavedList()
}