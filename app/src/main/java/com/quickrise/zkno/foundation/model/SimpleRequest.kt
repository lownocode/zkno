package com.quickrise.zkno.foundation.model

data class AppSendError(
    val error: String
)

data class ScheduleDateBody(
    val date: String
)

data class SignUpBody(
    val login: String,
    val password: String,
    val code: String,
)

data class SignInBody(
    val login: String,
    val password: String,
)

data class GetMarksBody(
    val semester: Int = 2,
    val year: Int = 2023,
) //TODO убрать явные год и семестр