package com.nilhcem.blenamebadge.util

import com.nilhcem.blenamebadge.data.BadgeConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiUtils private constructor() {
    companion object {
        private var adapter: JsonAdapter<BadgeConfig>? = null
        fun getAdapter(): JsonAdapter<BadgeConfig> {
            if (adapter == null) {
                adapter = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                    .adapter(BadgeConfig::class.java)
            }
            return adapter as JsonAdapter<BadgeConfig>
        }
    }
}