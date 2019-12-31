package org.fossasia.badgemagic.helpers

import org.fossasia.badgemagic.data.BadgeConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

object JSONHelper {
    fun decodeJSON(badgeJSON: String) = Json(JsonConfiguration.Stable).parse(BadgeConfig.serializer(),badgeJSON)
    fun encodeJSON(badgeData: BadgeConfig) = Json(JsonConfiguration.Stable).stringify(BadgeConfig.serializer(),badgeData)
}