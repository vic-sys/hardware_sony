/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.display

import android.os.Bundle
import android.util.ArraySet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.hardware.display.ColorDisplayManager

import androidx.preference.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

import com.android.internal.util.ArrayUtils
import com.android.settingslib.widget.LayoutPreference

import java.util.*
import kotlin.collections.ArrayList

import com.xperia.settings.display.R

import com.xperia.settings.preferences.SecureSettingSwitchPreference

const val CREATOR_MODE_KEY = "switchCreatorMode"

const val KEY_X_REALITY_ENGINE = "x_reality_engine_mode_enabled"
const val KEY_COLOR_BALANCE_RED = "color_balance_red"
const val KEY_COLOR_BALANCE_GREEN = "color_balance_green"
const val KEY_COLOR_BALANCE_BLUE = "color_balance_blue"

class DisplaySettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {
    private lateinit var creatorModeUtils: CreatorModeUtils
    private var colorBalanceRed: SeekBarPreference? = null
    private var colorBalanceGreen: SeekBarPreference? = null
    private var colorBalanceBlue: SeekBarPreference? = null

companion object {
    private const val DOT_INDICATOR_SIZE = 12
    private const val DOT_INDICATOR_LEFT_PADDING = 6
    private const val DOT_INDICATOR_RIGHT_PADDING = 6
}

    private val KEY_CREATOR_MODE_PREVIEW = "creator_mode_preview"
    private val PAGE_VIEWER_SELECTION_INDEX = "page_viewer_selection_index"

    private var mViewArrowPrevious: View? = null
    private var mViewArrowNext: View? = null
    private var mViewPager: ViewPager? = null

    private var mPageList: ArrayList<View>? = null

    private var mDotIndicators: Array<ImageView?>? = null
    private var mViewPagerImages: Array<View?>? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.display_settings)
        creatorModeUtils = CreatorModeUtils(context)

        addViewPager()
    
        colorBalanceRed = findPreference<SeekBarPreference>(KEY_COLOR_BALANCE_RED)
        colorBalanceGreen = findPreference<SeekBarPreference>(KEY_COLOR_BALANCE_GREEN)
        colorBalanceBlue = findPreference<SeekBarPreference>(KEY_COLOR_BALANCE_BLUE)
    
        val creatorModePreference = findPreference<SecureSettingSwitchPreference>(CREATOR_MODE_KEY)!!
        creatorModePreference.isChecked = creatorModeUtils.isEnabled
        creatorModePreference.onPreferenceChangeListener = this
    
        val xRealityEnginePreference = findPreference<SecureSettingSwitchPreference>(KEY_X_REALITY_ENGINE)!!
        val isXRealityEngineEnabled = xRealityEnginePreference.sharedPreferences?.getBoolean(KEY_X_REALITY_ENGINE, false) ?: false
        xRealityEnginePreference.isChecked = isXRealityEngineEnabled
        xRealityEnginePreference.onPreferenceChangeListener = this
    
        updateSeekBarsState(isXRealityEngineEnabled)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        when(preference.key) {
            CREATOR_MODE_KEY -> creatorModeUtils.setMode(newValue as Boolean)
            KEY_X_REALITY_ENGINE -> {
                updateSeekBarsState(newValue as Boolean)
                val sharedPreferences = preference.sharedPreferences
                sharedPreferences?.edit()?.putBoolean(KEY_X_REALITY_ENGINE, newValue as Boolean)?.apply()
            }
        }

        return true
    }

    private fun updateSeekBarsState(isXRealityEngineEnabled: Boolean) {
        colorBalanceRed?.isEnabled = !isXRealityEngineEnabled
        colorBalanceGreen?.isEnabled = !isXRealityEngineEnabled
        colorBalanceBlue?.isEnabled = !isXRealityEngineEnabled
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE_VIEWER_SELECTION_INDEX, mViewPager!!.currentItem)
    }

    fun getViewPagerResource(): ArrayList<Int> {
        return arrayListOf(
            R.layout.creator_mode_view1,
            R.layout.creator_mode_view2,
            R.layout.creator_mode_view3)
    }

    fun addViewPager() {
        val preview = findPreference<LayoutPreference>(KEY_CREATOR_MODE_PREVIEW)
        val tmpviewPagerList = getViewPagerResource()
        mViewPager = preview!!.findViewById(R.id.viewpager)

        mViewPagerImages = arrayOfNulls<View>(3)
        for (idx in tmpviewPagerList.indices) {
            mViewPagerImages!![idx] = layoutInflater.inflate(tmpviewPagerList[idx], null /* root */)
        }

        mPageList = ArrayList()
        mPageList!!.add(mViewPagerImages!![0]!!)
        mPageList!!.add(mViewPagerImages!![1]!!)
        mPageList!!.add(mViewPagerImages!![2]!!)

        mViewPager!!.adapter = ColorPagerAdapter(mPageList!!)

        mViewArrowPrevious = preview.findViewById(R.id.arrow_previous)
        mViewArrowPrevious?.setOnClickListener { v ->
            val previousPos = mViewPager!!.currentItem - 1
            mViewPager!!.setCurrentItem(previousPos, true)
        }

        mViewArrowNext = preview.findViewById(R.id.arrow_next)
        mViewArrowNext?.setOnClickListener { v ->
            val nextPos = mViewPager!!.currentItem + 1
            mViewPager!!.setCurrentItem(nextPos, true)
        }

        mViewPager!!.addOnPageChangeListener(createPageListener())

        val viewGroup = preview.findViewById<ViewGroup>(R.id.viewGroup)
        mDotIndicators = arrayOfNulls(mPageList!!.size)
        for (i in mPageList!!.indices) {
            val imageView = ImageView(context)
            val lp = ViewGroup.MarginLayoutParams(DOT_INDICATOR_SIZE, DOT_INDICATOR_SIZE)
            lp.setMargins(DOT_INDICATOR_LEFT_PADDING, 0, DOT_INDICATOR_RIGHT_PADDING, 0)
            imageView.layoutParams = lp
            mDotIndicators!![i] = imageView

            viewGroup.addView(mDotIndicators!![i])
        }

        updateIndicator(mViewPager!!.currentItem)
    }

    fun createPageListener(): ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset != 0f) {
                    for (idx in mPageList!!.indices) {
                        mViewPagerImages!![idx]!!.setVisibility(View.VISIBLE)
                    }
                } else {
                    mViewPagerImages!![position]!!.contentDescription =
                    getContext().getString(R.string.creator_mode_content_description)
                    updateIndicator(position)
                }
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        }
    }

    private fun updateIndicator(position: Int) {
        for (i in mPageList!!.indices) {
            if (position == i) {
                mDotIndicators!![i]!!.setBackgroundResource(R.drawable.ic_creator_mode_indicator_focused)
                mViewPagerImages!![i]!!.visibility = View.VISIBLE
            } else {
                mDotIndicators!![i]!!.setBackgroundResource(R.drawable.ic_creator_mode_indicator_unfocused)
                mViewPagerImages!![i]!!.visibility = View.INVISIBLE
            }
        }

        if (position == 0) {
            mViewArrowPrevious!!.visibility = View.INVISIBLE
            mViewArrowNext!!.visibility = View.VISIBLE
        } else if (position == (mPageList!!.size - 1)) {
            mViewArrowPrevious!!.visibility = View.VISIBLE
            mViewArrowNext!!.visibility = View.INVISIBLE
        } else {
            mViewArrowPrevious!!.visibility = View.VISIBLE
            mViewArrowNext!!.visibility = View.VISIBLE
        }
    }

    class ColorPagerAdapter(private val mPageViewList: ArrayList<View>) : PagerAdapter() {
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (mPageViewList[position] != null) {
                container.removeView(mPageViewList[position])
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mPageViewList[position])
            return mPageViewList[position]
        }

        override fun getCount(): Int {
            return mPageViewList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return `object` == view
        }
    }
}