/*
 * Copyright (C) 2024 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.charger
 
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.preference.*
import com.android.settingslib.widget.MainSwitchPreference

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

import com.xperia.settings.charger.R
import com.xperia.settings.charger.widgets.CustomSeekBarPreference

const val CHARGER_SETTING_ENABLE_KEY = "charger_setting_main_enable"
const val CHARGER_CHARGING_ENABLE_KEY = "device_charging_enable"
const val CHARGER_CHARGING_ENABLE_BACKUP = "device_charging_enable_backup"
const val CHARGER_CHARGING_LIMIT_KEY = "device_charging_control"
const val CHARGER_CHARGING_LIMIT_BACKUP = "device_charging_control_backup"

class ChargerSettingsFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener, CompoundButton.OnCheckedChangeListener { 

    private lateinit var chargerUtils: ChargerUtils

    private var mSwitch: MainSwitchPreference? = null
    private var mChargingSwitch: SwitchPreference? = null
    private var mChargingLimit: CustomSeekBarPreference? = null

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        Log.i(TAG, "${preference.key} has changed.")
        when (preference.key) {
            CHARGER_CHARGING_ENABLE_KEY -> {
                Log.i(TAG, "Charge enable: $newValue")
                chargerUtils.isChargingEnabled = newValue as Boolean
            }
            CHARGER_CHARGING_LIMIT_KEY -> chargerUtils.chargingLimit = newValue as Int
        }
        return true
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mSwitch?.isChecked = isChecked

        val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext())

        if (!isChecked) {
            val prefChargingEnabled = sharedPreferences.getBoolean(CHARGER_CHARGING_ENABLE_KEY, true)
            val prefChargingLimit = sharedPreferences.getInt(CHARGER_CHARGING_LIMIT_KEY, 100)
            sharedPreferences.edit().putBoolean(CHARGER_CHARGING_ENABLE_BACKUP, prefChargingEnabled).apply()
            sharedPreferences.edit().putInt(CHARGER_CHARGING_LIMIT_BACKUP, prefChargingLimit).apply()

            chargerUtils.isChargingEnabled = true
            mChargingSwitch?.isChecked = true
            chargerUtils.chargingLimit = 100
            mChargingLimit?.setValue(100, true)
        } else {
            val prefChargingEnabled = sharedPreferences.getBoolean(CHARGER_CHARGING_ENABLE_BACKUP, true)
            val prefChargingLimit = sharedPreferences.getInt(CHARGER_CHARGING_LIMIT_BACKUP, 100)

            chargerUtils.isChargingEnabled = prefChargingEnabled
            mChargingSwitch?.isChecked = prefChargingEnabled
            chargerUtils.chargingLimit = prefChargingLimit
            mChargingLimit?.setValue(prefChargingLimit, true)
        }

        Log.i(TAG, "Main charger switch toggled to $isChecked")
        chargerUtils.mainSwitch = isChecked
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.charger_settings, rootKey)
        mSwitch = findPreference(CHARGER_SETTING_ENABLE_KEY)
        mChargingSwitch = findPreference(CHARGER_CHARGING_ENABLE_KEY)
        mChargingLimit = findPreference(CHARGER_CHARGING_LIMIT_KEY)
    }

    companion object {
        private const val TAG = "ChargerSettings"
    }

    private fun addViewPager() {
    }
}