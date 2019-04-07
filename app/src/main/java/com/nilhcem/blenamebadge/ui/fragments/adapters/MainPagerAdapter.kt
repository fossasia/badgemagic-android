package com.nilhcem.blenamebadge.ui.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nilhcem.blenamebadge.ui.fragments.MainBitmapFragment
import com.nilhcem.blenamebadge.ui.fragments.MainTextFragment

class MainPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainTextFragment.newInstance("Hello", "World")
            else -> MainBitmapFragment.newInstance("Hello", "World")
        }
    }

    override fun getCount(): Int {
        return 2
    }
}