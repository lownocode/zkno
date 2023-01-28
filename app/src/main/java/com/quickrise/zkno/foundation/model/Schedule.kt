package com.quickrise.zkno.foundation.model

data class ScheduleModel(
    val id: Byte,
    val type: String,
    val name: String? = null,
    val start: String,
    val afterPause: AfterPause? = null,
    val teacher: String? = null,
    val classroom: String? = null,
    val end: String,
    val replacement: String? = null,
    val homework: Homework? = null,
    val marks: ArrayList<String>,
    val isEnded: Boolean,
    val topic: String?,
) {
    data class AfterPause(
        val end: String,
        val start: String,
        val duration: Byte,
    )

    data class Homework(
        val text: String,
        val files: ArrayList<String?>
    )
}