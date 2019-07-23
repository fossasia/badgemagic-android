package org.fossasia.badgemagic.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_edit_clipart.*
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.ActivityEditClipartBinding
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.StorageUtils
import org.fossasia.badgemagic.viewmodels.EditClipartViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditClipartActivity : AppCompatActivity() {

    private val editClipartViewModel by viewModel<EditClipartViewModel>()
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityDrawBinding = DataBindingUtil.setContentView<ActivityEditClipartBinding>(this, R.layout.activity_edit_clipart)
        activityDrawBinding.viewModel = editClipartViewModel

        if (intent.hasExtra("fileName")) {
            fileName = intent?.extras?.getString("fileName") ?: ""
            editClipartViewModel.drawingJSON.set(Converters.convertDrawableToLEDHex(StorageUtils.getClipartFromPath(fileName), false))
        }

        editClipartViewModel.savedButton.observe(this, Observer {
            if (it) {
                if (StorageUtils.saveEditedClipart(Converters.convertStringsToLEDHex(draw_layout.getCheckedList()), fileName)) {
                    Toast.makeText(this, R.string.clipart_saved_success, Toast.LENGTH_LONG).show()
                    editClipartViewModel.updateClipArts()
                } else
                    Toast.makeText(this, R.string.clipart_saved_error, Toast.LENGTH_LONG).show()
                finish()
            }
        })

        editClipartViewModel.resetButton.observe(this, Observer {
            if (it) {
                draw_layout.resetCheckListWithDummyData()
            }
        })
    }
}
