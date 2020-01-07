package org.fossasia.badgemagic.util

import org.fossasia.badgemagic.data.badge.Badges
import org.fossasia.badgemagic.data.badge.DeviceID
import org.koin.core.KoinComponent
import org.koin.core.inject

class BadgeUtils : KoinComponent {
    private val prefUtils: PreferenceUtils by inject()

    val currentDevice: DeviceID
        get() = Badges.valueOf(prefUtils.selectedBadge).device
}
