package com.nilhcem.blenamebadge.ui.badge_preview

import android.support.annotation.NonNull
import java.util.ArrayList


internal class CheckList {
    var list: ArrayList<Boolean> = ArrayList()

    @NonNull
    override fun toString(): String {
        return list.toString()
    }
}
