package com.nilhcem.blenamebadge.core.android.viewbinding

import android.app.Activity
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <V : View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> = required(id, viewFinder)

private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing = throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) =
        Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}
