package org.fossasia.badgemagic.bindings

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import org.fossasia.badgemagic.viewmodels.SettingsViewModel

@BindingAdapter("createAdapterFrom")
fun setSpinnerAdapter(spinner: AppCompatSpinner, viewModel: SettingsViewModel) {
    val list: MutableList<String> = viewModel.languageList.get() ?: mutableListOf()
    spinner.adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_dropdown_item, list)
    spinner.setSelection(viewModel.getSelectedSpinnerLanguage(), false)
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.setSelectedSpinnerLangauge(position)
        }
    }
}