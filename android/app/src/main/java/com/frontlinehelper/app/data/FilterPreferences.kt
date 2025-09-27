package com.frontlinehelper.app.data

data class FilterPreferences(
    val ownerId: String,
    val schools: List<String> = emptyList(),
    val dates: List<String> = emptyList(),
    val dayLengths: List<String> = emptyList(),
    val classes: List<String> = emptyList(),
    val teachers: List<String> = emptyList(),
    val subjects: List<String> = emptyList(),
    val pollingIntervalSeconds: Long = 60
)
