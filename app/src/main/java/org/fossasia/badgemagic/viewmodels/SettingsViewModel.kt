package org.fossasia.badgemagic.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.Language
import org.fossasia.badgemagic.util.PreferenceUtils

class SettingsViewModel(
    private val preferenceUtils: PreferenceUtils
) : ViewModel() {
    var languageList: ObservableField<MutableList<String>> = ObservableField()
    var changedLanguage: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val mutableList = mutableListOf<String>()
        mutableList.addAll(Language.values().map { it.toString() })
        languageList.set(mutableList)
    }

    fun getSelectedSpinnerLanguage() = preferenceUtils.selectedLanguage

    fun setSelectedSpinnerLangauge(position: Int) {
        preferenceUtils.selectedLanguage = position
        changedLanguage.value = true
    }
}