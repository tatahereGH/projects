package com.frontlinehelper.app.data

data class AcceptedJob(
    val jobId: String,
    val acceptedAt: Long = System.currentTimeMillis(),
    val acceptResult: String,
    val acceptMessage: String? = null
)
