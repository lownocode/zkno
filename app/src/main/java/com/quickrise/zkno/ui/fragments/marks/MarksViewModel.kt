package com.quickrise.zkno.ui.fragments.marks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.foundation.model.MarksItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarksViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _marksList = MutableLiveData<ArrayList<MarksItem>>()
    val marksList: MutableLiveData<ArrayList<MarksItem>> = _marksList

    init {
        getMarks()
    }

    private fun getMarks() = viewModelScope.launch {
        val marks = withContext(Dispatchers.IO) {
            apiRepository.getMarks()
        }

        if (marks.code() != 200) return@launch

        _marksList.value = marks.body()
    }
}