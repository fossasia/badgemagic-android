package org.fossasia.badgemagic.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.draw_layout.DrawMode
import org.fossasia.badgemagic.ui.custom.DrawBadgeLayout
import org.fossasia.badgemagic.util.SendingUtils

@BindingAdapter("drawState")
fun setBadgeDrawState(badge: DrawBadgeLayout, drawModeState: ObservableField<DrawMode>) {
    drawModeState.get()?.let { badge.changeDrawState(it) }
}

@BindingAdapter("drawingBadgeJSON")
fun setEditBadgeValues(badge: DrawBadgeLayout, drawJSON: ObservableField<String>) {
    val badgeConfig = SendingUtils.getBadgeFromJSON(drawJSON.get() ?: "{}")
    badgeConfig?.hexStrings?.let { badge.setValue(it) }
}

@BindingAdapter("drawingClipartJSON")
fun setEditClipartValues(badge: DrawBadgeLayout, drawJSON: ObservableField<List<String>>) {
    drawJSON.get()?.let { badge.setValue(it) }
}

@BindingAdapter("changeColor")
fun changeColorState(imageView: ImageView, isEnabled: ObservableBoolean) {
    imageView.setColorFilter(
        if (isEnabled.get())
            imageView.context.resources.getColor(R.color.colorAccent)
        else
            imageView.context.resources.getColor(android.R.color.black)
    )
}
