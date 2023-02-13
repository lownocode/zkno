package com.quickrise.zkno.ui.fragments.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quickrise.zkno.Key
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.withActivity

class SettingsViewModel : ViewModel() {
    private val _themeMode = MutableLiveData<String>()
    val themeMode: MutableLiveData<String> = _themeMode

    init {
        getDarkThemeMode()
    }

    private fun getDarkThemeMode() = withActivity { activity ->
        _themeMode.value = Preferences(activity).settings?.getString(Key.PREF_DARK_THEME_MODE, Key.MODE_ON)
    }

    fun changeThemeMode(mode: String) = withActivity { activity ->
        Preferences(activity)
            .settings
            ?.edit()
            ?.putString(Key.PREF_DARK_THEME_MODE, mode)
            ?.apply()

        _themeMode.value = mode
    }
}