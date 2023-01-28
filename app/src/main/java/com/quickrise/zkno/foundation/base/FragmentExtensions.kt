package com.quickrise.zkno.foundation.base

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