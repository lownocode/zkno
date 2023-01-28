package com.quickrise.zkno.api

import com.quickrise.zkno.foundation.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth.signIn")
    suspend fun signIn(
        @Header("Authorization") authorization: String?,
        @Header("App-Version") appVersion: String,
        @Header("Notify-Token") notifyToken: String?,
    ): Response<UserModel>

    @POST("auth.signIn")
    suspend fun signIn(
        @Header("App-Version") appVersion: String,
        @Header("Notify-Token") notifyToken: String?,
        @Body body: SignInBody
    ): Response<UserModel>

    @POST("auth.signUp")
    suspend fun signUp(
        @Header("App-Version") appVersion: String,
        @Header("Notify-Token") notifyToken: String?,
        @Body body: SignUpBody
    ): Response<UserModel>

    @POST("schedule.get")
    suspend fun getSchedule(
        @Header("Authorization") authorization: String?,
        @Body date: ScheduleDateBody
    ): Response<ArrayList<ScheduleModel>>

    @POST("app.sendReport")
    suspend fun appSendReport(
        @Header("App-Version") appVersion: String,
        @Header("Authorization") authorization: String?,
        @Body error: AppSendError
    ): Response<Boolean>

    @POST("marks.get")
    suspend fun getMarks(
        @Header("Authorization") authorization: String?,
    ): Response<ArrayList<MarksItem>>

    @GET("dinners/{pathToImage}")
    suspend fun getDinnerImage(
        @Path("pathToImage") pathToImage: String
    ): Response<ResponseBody>
}