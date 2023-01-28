package com.quickrise.zkno.foundation.base

import android.app.Activity
import com.quickrise.zkno.api.ApiRepository

@Suppress("unused")
fun Activity.viewModelFactory() = ViewModelFactory(
    apiRepository = ApiRepository(this),
    app = this.application
)