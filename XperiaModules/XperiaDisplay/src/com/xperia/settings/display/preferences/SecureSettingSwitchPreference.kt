/*
* Copyright (C) 2024 XperiaLabs Project
* Copyright (C) 2016-2018 crDroid Android Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.xperia.settings.preferences

import android.content.Context
import android.provider.Settings
import android.os.UserHandle
import android.util.AttributeSet
import androidx.preference.SwitchPreferenceCompat

class SecureSettingSwitchPreference : SwitchPreferenceCompat {

   constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
   constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
   constructor(context: Context) : super(context)

   override fun persistBoolean(value: Boolean): Boolean {
       putBoolean(key, value)
       return true
   }

   private fun putBoolean(key: String?, value: Boolean) {
       if (key != null) {
           Settings.Secure.putIntForUser(context.contentResolver, key, if (value) 1 else 0, UserHandle.USER_CURRENT)
       }
   }

   override fun getPersistedBoolean(defaultReturnValue: Boolean): Boolean {
       return getBoolean(key, defaultReturnValue)
   }

   private fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
       return if (key != null) {
           Settings.Secure.getIntForUser(context.contentResolver, key, if (defaultValue) 1 else 0, UserHandle.USER_CURRENT) != 0
       } else {
           defaultValue
       }
   }
}