package com.nilhcem.blenamebadge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.nilhcem.blenamebadge.R
import com.nilhcem.blenamebadge.ui.fragments.adapters.MainPagerAdapter
import com.nilhcem.blenamebadge.ui.fragments.interfaces.PreviewChangeListener
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity(), PreviewChangeListener {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        setupViewPager()
        setupBottomNavigationMenu()
    }

    override fun onPreviewChange() {
    }

    private fun setupViewPager() {
        pagerAdapter = MainPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupBottomNavigationMenu() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.textFragment -> viewPager.currentItem = 0
                R.id.bitmapFragment -> viewPager.currentItem = 1
                else -> viewPager.currentItem = 0
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
