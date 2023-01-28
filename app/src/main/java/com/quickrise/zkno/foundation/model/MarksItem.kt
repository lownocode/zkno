package com.quickrise.zkno.foundation.model

data class MarksItem(
    val subjectName: String,
    val marks: ArrayList<Marks>,
    val avg: Float,
) {
    data class Marks(
        val date: String,
        val mark: String
    )
}
