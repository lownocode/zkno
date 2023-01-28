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