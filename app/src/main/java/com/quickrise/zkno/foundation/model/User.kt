package com.quickrise.zkno.foundation.model

data class UserModel(
    val name: String,
    val photo: String? = null,
    val group: Group? = null,
    val newAppVersion: NewAppVersion? = null,
    val token: String,
    val bindings: Bindings
) {
    data class Group(
        val name: String,
        val specialty: String,
        val curator: String,
        val location: Location? = null,
    )

    data class Location (
        val address: String?,
        val coordinates: String? = null,
    )

    data class NewAppVersion(
        val name: String,
        val code: Int,
        val path: String,
        val date: String,
        val whatsNew: String,
        val isRequired: Boolean,
    )

    data class Bindings(
        val code: String,
        val vk: Boolean,
        val tg: Boolean
    )
}