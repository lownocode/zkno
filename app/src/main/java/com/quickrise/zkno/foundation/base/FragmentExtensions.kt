package com.quickrise.zkno.foundation.base

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.navigator.ViewModelNavigator

@Suppress("unused")
fun Fragment.viewModelFactory() = ViewModelFactory(
    apiRepository = ApiRepository(requireActivity()),
    app = requireActivity().application
)

fun Fragment.navigator(): ViewModelNavigator =
    ViewModelProvider(
        requireActivity(),
        AndroidViewModelFactory(requireActivity().application)
    )[ViewModelNavigator::class.java]

fun Fragment.log(
    text: String,
    tag: String = "CUSTOM LOG =>",
    logLevel: String = "d"
) {
    val className = "[${this::class.java.simpleName}]\n"

    when (logLevel) {
        "e" ->  Log.e(tag, className + text)
        "i" ->  Log.i(tag, className + text)
        "v" ->  Log.v(tag, className + text)
        "w" ->  Log.w(tag, className + text)
        else -> Log.d(tag, className + text)
    }
}