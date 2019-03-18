package com.nilhcem.blenamebadge.ui.message

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nilhcem.blenamebadge.R

class SplashActivity : Activity() {

    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(/*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    val i = Intent(this@SplashActivity, MessageActivity::class.java)
                    startActivity(i)

                    // close this activity
                    finish()
                }, SPLASH_TIME_OUT)
    }

}
