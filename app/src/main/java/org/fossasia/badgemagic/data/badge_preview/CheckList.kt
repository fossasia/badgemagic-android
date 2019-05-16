package org.fossasia.badgemagic.data.badge_preview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckList(var list: MutableList<Boolean> = mutableListOf()) : Parcelable
