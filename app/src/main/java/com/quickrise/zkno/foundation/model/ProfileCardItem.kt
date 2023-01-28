package com.quickrise.zkno.foundation.model

data class ProfileCardItem(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val actionIcon: Int? = null,
    val actionIconOnClick: (() -> Unit)? = null
)
