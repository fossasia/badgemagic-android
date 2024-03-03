package org.fossasia.badgemagic.helpers

import kotlinx.serialization.encodeToString
import org.fossasia.badgemagic.data.BadgeConfig
import kotlinx.serialization.json.Json

object JSONHelper {
    private val jsonFormat = Json { encodeDefaults = true }

    fun decodeJSON(badgeJSON: String): BadgeConfig = Json.decodeFromString(badgeJSON)
    fun encodeJSON(badgeData: BadgeConfig) = jsonFormat.encodeToString(badgeData)
}
