package com.nilhcem.blenamebadge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.nilhcem.blenamebadge.core.android.log.Timber
import com.nilhcem.blenamebadge.core.android.viewbinding.bindView

class MainActivity : AppCompatActivity() {

    val content: EditText by bindView(R.id.text_to_send)
    val send: Button by bindView(R.id.send_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        send.setOnClickListener {
            Timber.i { "Text to send: ${content.text.trim()}" }
        }
    }
}
