package org.fossasia.badgemagic.extensions

import android.app.Activity
import androidx.annotation.Keep

@Keep
fun <A : Activity> A.setRotation(rotation: Int) {
    this.requestedOrientation = rotation
}
