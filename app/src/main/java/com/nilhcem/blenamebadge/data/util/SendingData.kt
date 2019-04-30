package com.nilhcem.blenamebadge.data.util

import com.nilhcem.blenamebadge.data.device.model.Mode
import com.nilhcem.blenamebadge.data.device.model.Speed

data class SendingData(val invertLED: Boolean, val flash: Boolean, val marquee: Boolean, val mode: Mode, val speed: Speed)
