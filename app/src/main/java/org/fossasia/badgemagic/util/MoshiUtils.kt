package org.fossasia.badgemagic.util

import org.fossasia.badgemagic.data.fragments.BadgeConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.fossasia.badgemagic.data.device.model.Mode
import org.fossasia.badgemagic.data.device.model.Speed

class MoshiUtils private constructor() {
    companion object {
        private var adapter: JsonAdapter<BadgeConfig>? = null
        fun getAdapter(): JsonAdapter<BadgeConfig> {
            if (adapter == null) {
                adapter = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(Mode.Adapter())
                    .add(Speed.Adapter())
                    .build()
                    .adapter(BadgeConfig::class.java)
            }
            return adapter as JsonAdapter<BadgeConfig>
        }
    }
}