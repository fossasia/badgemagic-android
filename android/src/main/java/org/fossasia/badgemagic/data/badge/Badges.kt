package org.fossasia.badgemagic.data.badge

import java.util.UUID

enum class Badges(val device: DeviceID) {
    LSLED(
        DeviceID(
            UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb")
        )
    ),
    VBLAB(
        DeviceID(
            UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
        )
    );
}
