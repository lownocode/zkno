package com.quickrise.zkno.ui.fragments.settings

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.quickrise.zkno.Key
import com.quickrise.zkno.Preferences

class SettingsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _themeMode = savedStateHandle.getLiveData<String>(MODE_KEY)
    val themeMode: LiveData<String> = _themeMode

    init {
        getDarkThemeMode()
    }

    private fun getDarkThemeMode() {
        val defaultMode = when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> Key.MODE_OFF
            else -> Key.MODE_AUTO
        }

        _themeMode.value = Preferences().settings?.getString(Key.DARK_THEME_MODE, defaultMode)
    }

    fun changeThemeMode(mode: String) {
        _themeMode.value = mode

        Preferences().settings?.edit()?.putString(Key.DARK_THEME_MODE, mode)?.apply()
    }

    companion object {
        const val MODE_KEY = "SETTINGS_MODE_KEY"
    }
}