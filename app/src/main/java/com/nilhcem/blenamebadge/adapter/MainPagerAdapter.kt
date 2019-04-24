package com.nilhcem.blenamebadge.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}