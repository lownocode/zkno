package com.quickrise.zkno

import com.quickrise.zkno.foundation.base.ActivityActions
import com.quickrise.zkno.foundation.model.UserModel
import com.quickrise.zkno.ui.activities.main.MainActivity

val withActivity = ActivityActions<MainActivity>()
val emptyUser = UserModel("", "", null, null, "")

const val API_BASE_URL = "https://zkno.ru/api/"
const val DIRECTION_PREVIOUS = "previous"
const val DIRECTION_NEXT = "next"

object RegexPattern {
    const val email = "((?!\\.)[\\w\\-_.]*[^.])@(\\w+)\\.(\\w+(\\.\\w+)?[^\\W])"
    const val url = "\\b(https?:\\/\\/)?(www\\.)?(?<!@)([aA-zZаА-яЯ0-9]+\\.)+[aA-zZаА-яЯ]{2,4}(\\/\\w+)?(\\?\\w+=.+)?\\b"
}

object Tab {
    const val PROFILE  = "profile"
    const val MARKS    = "marks"
    const val SCHEDULE = "schedule"
    const val DINNER   = "dinner"
}

object FragmentIndex {
    const val PROFILE  = 0
    const val MARKS    = 1
    const val SCHEDULE = 2
    const val DINNER   = 3
    const val SETTINGS = 4
    const val ABOUT    = 5
}

object Key {
    const val MODE_ON         = "ON"
    const val MODE_OFF        = "OFF"
    const val MODE_AUTO       = "AUTO"
    const val DARK_THEME_MODE = "darkThemeMode"
    const val MENU_IS_COMPACT = "compactBottomNav"
}

val notificationChannels = listOf(
    "main"         to "Общие",
    "dinner"       to "Обед",
    "marks"        to "Оценки",
    "replacements" to "Замены",
    "homeworks"    to "Домашние задания",
)