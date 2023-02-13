package com.quickrise.zkno.ui.fragments.profile

import androidx.lifecycle.*
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.foundation.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: MutableLiveData<UserModel> = _user

    init {
        getUserData()
    }

    private fun getUserData() = viewModelScope.launch {
        val userData = withContext(Dispatchers.IO) {
            apiRepository.signIn()
        }

        if (userData.code() != 200) return@launch

        userData.body()?.let {
            com.quickrise.zkno.user = it
            _user.value = it
        }
    }
}