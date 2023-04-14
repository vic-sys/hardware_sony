#
# Copyright (C) 2024 XperiaLabs Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Flags
TARGET_SHIPS_XPERIA_SETTINGS ?= true
TARGET_SHIPS_XPERIA_SETTINGS_MENU ?= true
TARGET_SUPPORTS_IMAGE_ENHANCEMENT ?= true
TARGET_SUPPORTS_HIGH_REFRESH_RATE ?= true
TARGET_SUPPORTS_SOUND_ENHANCEMENT ?= true
TARGET_SUPPORTS_SOUND_ENHANCEMENT_DTS ?= true
TARGET_SUPPORTS_BATTERY_CARE ?= true
TARGET_SUPPORTS_EUICC ?= true

# Soong Namespace
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)/XperiaModules

# Main Module
ifeq ($(TARGET_SHIPS_XPERIA_SETTINGS),true)
PRODUCT_PACKAGES += XperiaSettings
endif

# Main Module - Main Settings Page
ifeq ($(TARGET_SHIPS_XPERIA_SETTINGS_MENU),true)
PRODUCT_PACKAGES += XperiaSettingsMenu
endif

# Submodules
ifeq ($(TARGET_SUPPORTS_IMAGE_ENHANCEMENT),true)
	PRODUCT_PACKAGES += XperiaDisplay
endif

ifeq ($(TARGET_SUPPORTS_HIGH_REFRESH_RATE),true)
	PRODUCT_PACKAGES += XperiaSwitcher
endif

ifeq ($(TARGET_SUPPORTS_SOUND_ENHANCEMENT),true)
	PRODUCT_PACKAGES += \
	XperiaAudio \
        XperiaAudioAddon
endif

ifeq ($(TARGET_SUPPORTS_SOUND_ENHANCEMENT_DTS),true)
	PRODUCT_PACKAGES += \
	XperiaAudioDTS \
	XperiaTSRA
endif

ifeq ($(TARGET_SUPPORTS_BATTERY_CARE),true)
include hardware/sony/XperiaModules/XperiaCharger/sepolicy/qti/SEPolicy.mk
	PRODUCT_PACKAGES += XperiaCharger
endif

ifeq ($(TARGET_SUPPORTS_EUICC),true)
	PRODUCT_PACKAGES += XperiaEuicc
endif
