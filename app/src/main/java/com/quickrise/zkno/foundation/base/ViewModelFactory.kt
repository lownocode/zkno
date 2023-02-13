package com.quickrise.zkno.foundation.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.ui.fragments.dinner.DinnerViewModel
import com.quickrise.zkno.ui.fragments.marks.MarksViewModel
import com.quickrise.zkno.ui.fragments.profile.ProfileViewModel
import com.quickrise.zkno.ui.fragments.schedule.ScheduleViewModel
import com.quickrise.zkno.ui.fragments.settings.SettingsViewModel

@Suppress("unchecked_cast")
class ViewModelFactory(
    private val app: Application,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        val viewModel = when(modelClass) {
            ProfileViewModel::class.java  -> ProfileViewModel(apiRepository)
            MarksViewModel::class.java    -> MarksViewModel(apiRepository)
            ScheduleViewModel::class.java -> ScheduleViewModel(
                app = app,
                savedStateHandle = savedStateHandle,
                apiRepository = apiRepository
            )
            DinnerViewModel::class.java   -> DinnerViewModel(
                app = app,
                savedStateHandle = savedStateHandle,
                apiRepository = apiRepository
            )
            SettingsViewModel::class.java -> SettingsViewModel()

            else -> throw IllegalStateException("Unknown ViewModel class")
        }

        return viewModel as T
    }
}