/*
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.parts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xperia.parts.display.CreatorModeUtils
import com.xperia.parts.charger.ChargerUtils
import com.xperia.parts.dirac.DiracUtils

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Starting")
        CreatorModeUtils(context).initialize()
        ChargerUtils(context).applyOnBoot()

        if (DEBUG) Log.d(TAG, "Received boot completed intent")

        val millis = 1 * 60 * 1000  // 1min
        try {
            Thread.sleep(millis.toLong())
            DiracUtils(context).onBootCompleted()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "XperiaParts"
        private const val DEBUG = false
    }
}