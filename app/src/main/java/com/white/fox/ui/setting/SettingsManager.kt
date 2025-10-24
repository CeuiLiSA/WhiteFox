package com.white.fox.ui.setting

import ceui.lisa.hermes.cache.PersistState

class SettingsManager : PersistState<AppSettings>(
    "Setting",
    AppSettings()
) {

}