package com.quickrise.zkno.ui.fragments.schedule

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.quickrise.zkno.*
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.foundation.model.ScheduleDateBody
import com.quickrise.zkno.foundation.model.ScheduleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ScheduleViewModel(
    savedStateHandle: SavedStateHandle,
    private val app: Application,
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _choosedDate = savedStateHandle.getLiveData(CHOOSED_DATE_KEY, System.currentTimeMillis())
    val choosedDate: LiveData<Long> = _choosedDate

    private val _scheduleErrorCode = savedStateHandle.getLiveData(SCHEDULE_ERROR_KEY, "none")
    val scheduleErrorCode: LiveData<String> = _scheduleErrorCode

    private val _scheduleList = MutableLiveData<ArrayList<ScheduleModel>>()
    val scheduleList: MutableLiveData<ArrayList<ScheduleModel>> = _scheduleList

    init {
        getSchedule()
    }

    private fun getSchedule() = viewModelScope.launch {
        val date = Utils.FormatDate().getDateTime(_choosedDate.value!!)

        val schedule = withContext(Dispatchers.IO) {
            apiRepository.getSchedule(ScheduleDateBody(date))
        }

        if (schedule.code() != 200) {
            val error = schedule.errorBody()?.string()?.let { JSONObject(it) }

            _scheduleErrorCode.value = error?.get("code").toString()

            return@launch
        }

        _scheduleErrorCode.value = "none"
        _scheduleList.value = schedule.body()!!
    }

    fun skipDay(direction: String) = when(direction) {
        DIRECTION_PREVIOUS -> {
            _choosedDate.value = _choosedDate.value?.minus(86_400_000)

            getSchedule()
        }
        else -> { /*the next direction*/
            _choosedDate.value = _choosedDate.value?.plus(86_400_000)

            getSchedule()
        }
    }

    fun updateDate(date: Long, withSchedule: Boolean = false) {
        _choosedDate.value = date

        if (withSchedule) getSchedule()
    }

    fun goToday(): Boolean {
        _choosedDate.value = System.currentTimeMillis()

        Toast.makeText(
            app.applicationContext,
            "Возвращаю на текущий день...",
            Toast.LENGTH_SHORT
        ).show()

        getSchedule()

        return true
    }

    companion object {
        const val CHOOSED_DATE_KEY = "SCHEDULE_CHOOSED_DATE"
        const val SCHEDULE_ERROR_KEY = "SCHEDULE_ERROR_CODE"
    }
}