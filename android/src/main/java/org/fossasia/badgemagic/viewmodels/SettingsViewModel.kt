package org.fossasia.badgemagic.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.Language
import org.fossasia.badgemagic.data.badge.Badges
import org.fossasia.badgemagic.util.PreferenceUtils

class SettingsViewModel(
    private val preferenceUtils: PreferenceUtils
) : ViewModel() {
    var languageList: ObservableField<MutableList<String>> = ObservableField()
    var changedLanguage: MutableLiveData<Boolean> = MutableLiveData()

    var badgesList: ObservableField<MutableList<String>> = ObservableField()
    var changedBadge: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val mutableLanguageList = mutableListOf<String>()
        mutableLanguageList.addAll(Language.values().map { it.toString() })
        languageList.set(mutableLanguageList)

        val mutableBadgeList = mutableListOf<String>()
        mutableBadgeList.addAll(Badges.values().map { it.toString() })
        badgesList.set(mutableBadgeList)
    }

    fun getSelectedSpinnerLanguage() = preferenceUtils.selectedLanguage

    fun setSelectedSpinnerLanguage(position: Int) {
        preferenceUtils.selectedLanguage = position
        changedLanguage.value = true
    }

    fun setSelectedSpinnerBadge(position: Int) {
        badgesList.get()?.let { preferenceUtils.selectedBadge = it[position] }
        changedBadge.value = true
    }
}
