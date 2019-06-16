package org.fossasia.badgemagic.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.ActivityDrawBinding
import org.fossasia.badgemagic.viewmodels.DrawViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawActivity : AppCompatActivity() {

    private val viewModel by viewModel<DrawViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityDrawBinding = DataBindingUtil.setContentView<ActivityDrawBinding>(this, R.layout.activity_draw)
        activityDrawBinding.viewModel = viewModel
    }
}
