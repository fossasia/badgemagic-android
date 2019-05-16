package org.fossasia.badgemagic.data

import androidx.annotation.DrawableRes
import org.fossasia.badgemagic.data.device.model.Mode

data class ModeInfo(@DrawableRes val drawableID: Int, val mode: Mode)