package com.frontlinehelper.app.data

data class AuditLogEntry(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actionType: String,
    val details: String? = null,
    val correlationId: String? = null
)
