package com.nilhcem.blenamebadge.ui.fragments.main_text

import androidx.lifecycle.ViewModel

class MainTextDrawableViewModel : ViewModel() {
    var speed = 1
    var isFlash = false
    var isMarquee = false
    var isInverted = false
    var drawablePosition = -1
    var animationPosition = -1
    var radioSelectedId = -1
    var currentTab = 1
    var text = ""
}