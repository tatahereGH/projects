package com.frontlinehelper.app.data

data class Job(
    val id: String,
    val school: String,
    val date: String,
    val dayLength: String,
    val className: String,
    val teacher: String,
    val subject: String,
    val description: String? = null,
    val sourceHtml: String? = null,
    val fetchedAt: Long = System.currentTimeMillis()
)
