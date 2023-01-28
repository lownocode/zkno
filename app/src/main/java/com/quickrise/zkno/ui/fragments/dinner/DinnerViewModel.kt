package com.quickrise.zkno.ui.fragments.dinner

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickrise.zkno.*
import com.quickrise.zkno.api.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat

class DinnerViewModel(
    savedStateHandle: SavedStateHandle,
    private val app: Application,
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _choosedDate = savedStateHandle.getLiveData(CHOOSED_DATE_KEY, System.currentTimeMillis())
    val choosedDate: LiveData<Long> = _choosedDate

    private val _dinnerErrorCode = savedStateHandle.getLiveData(DINNER_ERROR_KEY, "none")
    val dinnerErrorCode: LiveData<String> = _dinnerErrorCode

    private val _dinnerImage = MutableLiveData<ByteArray>()
    val dinnerImage: MutableLiveData<ByteArray> = _dinnerImage

    init {
        loadDinnerImage()
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadDinnerImage() = viewModelScope.launch {
        val pictureTheme = if (Utils.isDarkTheme()) "_dark" else ""
        val date = SimpleDateFormat("dd.MM.yy").format(_choosedDate.value)
        val path = "$date$pictureTheme.png"

        val image = withContext(Dispatchers.IO) {
            apiRepository.getDinnerImage(path)
        }

        if (image.code() != 200) {
            val error = image.errorBody()?.string()?.let { JSONObject(it) }

            _dinnerErrorCode.value = error?.get("code").toString()

            return@launch
        }

        _dinnerErrorCode.value = "none"
        _dinnerImage.value = image.body()!!.bytes()
    }

    fun skipDay(direction: String) = when(direction) {
        DIRECTION_PREVIOUS -> {
            _choosedDate.value = _choosedDate.value?.minus(86_400_000)

            loadDinnerImage()
        }
        else -> { /*the next direction*/
            _choosedDate.value = _choosedDate.value?.plus(86_400_000)

            loadDinnerImage()
        }
    }

    fun updateDate(date: Long, withDinnerImage: Boolean = false) {
        _choosedDate.value = date

        if (withDinnerImage) loadDinnerImage()
    }

    fun goToday(): Boolean {
        _choosedDate.value = System.currentTimeMillis()

        Toast.makeText(
            app.applicationContext,
            "Возвращаю на текущий день...",
            Toast.LENGTH_SHORT
        ).show()

        loadDinnerImage()

        return true
    }

    companion object {
        const val CHOOSED_DATE_KEY = "DINNER_CHOOSED_DATE"
        const val DINNER_ERROR_KEY = "DINNER_ERROR_CODE"
    }
}