package org.fossasia.badgemagic.data.util

import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed

data class SendingData(val invertLED: Boolean, val flash: Boolean, val marquee: Boolean, val mode: Mode, val speed: Speed)
