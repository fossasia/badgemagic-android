package org.fossasia.badgemagic.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.draw_layout.DrawMode
import org.fossasia.badgemagic.ui.custom.DrawBadgeLayout

@BindingAdapter("drawState")
fun setBadgeDrawState(badge: DrawBadgeLayout, drawModeState: ObservableField<DrawMode>) {
    drawModeState.get()?.let { badge.changeDrawState(it) }
}

@BindingAdapter("resetState")
fun resetDrawBadge(badge: DrawBadgeLayout, isEnabled: ObservableBoolean) {
    badge.resetCheckList()
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