package org.fossasia.badgemagic.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_edit_badge.*
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.data.fragments.BadgeConfig
import org.fossasia.badgemagic.databinding.ActivityEditBadgeBinding
import org.fossasia.badgemagic.util.Converters
import org.fossasia.badgemagic.util.SendingUtils
import org.fossasia.badgemagic.util.StorageUtils
import org.fossasia.badgemagic.viewmodels.EditBadgeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditBadgeActivity : AppCompatActivity() {

    private val viewModel by viewModel<EditBadgeViewModel>()
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityDrawBinding = DataBindingUtil.setContentView<ActivityEditBadgeBinding>(this, R.layout.activity_edit_badge)
        activityDrawBinding.viewModel = viewModel

        if (intent.hasExtra("badgeJSON") && intent.hasExtra("fileName")) {
            viewModel.drawingJSON.set(intent?.extras?.getString("badgeJSON"))
            fileName = intent?.extras?.getString("fileName") ?: ""
        }

        viewModel.savedButton.observe(this, Observer {
            if (it) {
                val badgeConfig = SendingUtils.getBadgeFromJSON(viewModel.drawingJSON.get() ?: "{}")
                badgeConfig?.hexStrings = Converters.convertBitmapToLEDHex(
                        Converters.convertStringsToLEDHex(draw_layout.getCheckedList()),
                        false
                )
                badgeConfig?.let { config -> StoreAsync(fileName, config, viewModel).execute() }
                Toast.makeText(this, R.string.saved_edited_badge, Toast.LENGTH_LONG).show()
                finish()
            }
        })

        viewModel.resetButton.observe(this, Observer {
            if (it) {
                draw_layout.resetCheckListWithDummyData()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.open_folder, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.open_Folder -> {
                val intent = Intent(this, DrawerActivity::class.java)
                intent.putExtra("badge", "badge")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        class StoreAsync(private val fileName: String, private val badgeConfig: BadgeConfig, private val viewModel: EditBadgeViewModel) : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                StorageUtils.saveEditedBadge(badgeConfig, fileName)
                return null
            }

            override fun onPostExecute(result: Void?) {
                viewModel.updateFiles()
                super.onPostExecute(result)
            }
        }
    }
}
