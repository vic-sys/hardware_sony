# hardware_sony Repository

## DISCLAIMER
- All Sony applications (from the stock ROM) are owned by and Sony™.

## How to Include hw_sony in your DT (Device Tree)?
- Inherit the extras repo (for your specific series) from your device.mk as shown
```
include hardware/sony/XperiaModules.mk
```

- Set Flags Accordingly in order to ship different modules of your choosing

|Flag|Description|
|:-:|:-:|
|`TARGET_SHIPS_XPERIA_SETTINGS`|`Ships the regular Xperia Settings package.`|
|`TARGET_SHIPS_XPERIA_SETTINGS_MENU`|`Ships a modified Xperia Settings that shows up on the main settings page. Broken on some ROMs, in those cases use TARGET_SHIPS_XPERIA_SETTINGS instead.`|
|`TARGET_SUPPORTS_IMAGE_ENHANCEMENT`|`Ships Creator Mode & X-Reality Engine.`|
|`TARGET_SUPPORTS_HIGH_REFRESH_RATE `|`Ships Xperia Switcher, which is used to enable a High Refresh Rate.`|
|`TARGET_SUPPORTS_HIGH_POLLING_RATE`|`Ships Xperia Touch, which is used to enable a High Polling Rate.`|
|`TARGET_SUPPORTS_SOUND_ENHANCEMENT `|`Ships various things needed for Dolby Sound and 360 Reality Audio Upmix.`|
|`TARGET_SUPPORTS_SOUND_ENHANCEMENT_DTS `|`Ships various things needed for DTS:X Ultra.`|
|`TARGET_SUPPORTS_BATTERY_CARE `|`As it says, this ships Battery Care (And H.S. Power Control, aka. "Pause Charging").`|
|`TARGET_SUPPORTS_EUICC `|`Ships XperiaEUICC, needed for devices which have eSIM support.`|