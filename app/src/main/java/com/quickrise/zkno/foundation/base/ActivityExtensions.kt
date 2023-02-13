package com.quickrise.zkno.foundation.base

import android.app.Activity
import android.util.Log
import com.quickrise.zkno.api.ApiRepository

@Suppress("unused")
fun Activity.viewModelFactory() = ViewModelFactory(
    apiRepository = ApiRepository(this),
    app = this.application
)

fun Activity.log(
    text: String,
    tag: String = "CUSTOM LOG --->",
    logLevel: String = "d"
) = when (logLevel) {
    "e" -> Log.e(tag, text)
    "i" -> Log.i(tag, text)
    "v" -> Log.v(tag, text)
    "w" -> Log.w(tag, text)
    else -> Log.d(tag, text)
}