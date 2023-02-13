package com.quickrise.zkno.api

import android.app.Activity
import com.quickrise.zkno.BuildConfig
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.foundation.model.*
import com.quickrise.zkno.foundation.model.UserModel
import okhttp3.ResponseBody
import retrofit2.Response

class ApiRepository(activity: Activity) {
    private val userToken = Preferences(activity).user?.getString("token", "defval")
    private val notifyToken = Preferences(activity).user?.getString("notifyToken", null)

    suspend fun signIn(body: SignInBody? = null): Response<UserModel> {
        if (body == null) {
            return RetrofitInstance.api.signIn(
                authorization = userToken,
                appVersion = BuildConfig.VERSION_CODE.toString(),
                notifyToken = notifyToken,
            )
        }

        return RetrofitInstance.api.signIn(
            appVersion = BuildConfig.VERSION_CODE.toString(),
            notifyToken = notifyToken,
            body = body
        )
    }

    suspend fun signUp(body: SignUpBody): Response<UserModel> {
        return RetrofitInstance.api.signUp(
            body = body,
            appVersion = BuildConfig.VERSION_CODE.toString(),
            notifyToken = notifyToken
        )
    }

    suspend fun getSchedule(date: ScheduleDateBody): Response<ArrayList<ScheduleModel>> {
        return RetrofitInstance.api.getSchedule(
            authorization = userToken,
            date = date
        )
    }

    suspend fun appSendReport(error: AppSendError): Response<Boolean> {
        return RetrofitInstance.api.appSendReport(
            authorization = userToken,
            error = error,
            appVersion = BuildConfig.VERSION_CODE.toString(),
        )
    }

    suspend fun getMarks(): Response<ArrayList<MarksItem>> {
        return RetrofitInstance.api.getMarks(
            authorization = userToken,
            body = GetMarksBody()//TODO поставить сюда нормальное тело
        )
    }

    suspend fun getDinnerImage(path: String): Response<ResponseBody> {
        return RetrofitInstance.api.getDinnerImage(
            pathToImage = path
        )
    }
}