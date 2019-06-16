package org.fossasia.badgemagic.ui.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.fossasia.badgemagic.util.LocaleManager

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }
}