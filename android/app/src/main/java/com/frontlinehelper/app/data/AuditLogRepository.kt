package com.frontlinehelper.app.data

object AuditLogRepository {
    private val entries = mutableListOf<AuditLogEntry>()

    fun write(entry: AuditLogEntry) {
        entries.add(entry)
    }

    fun list(): List<AuditLogEntry> = entries.toList()

    fun clear() { entries.clear() }

    fun exportCsv(): String {
        val header = "id,timestamp,actionType,details,correlationId"
        val rows = entries.joinToString("\n") { e ->
            listOf(e.id, e.timestamp.toString(), e.actionType, (e.details ?: ""), (e.correlationId ?: "")).joinToString(",")
        }
        return header + "\n" + rows
    }

    fun pruneOlderThan(days: Int) {
        val cutoff = System.currentTimeMillis() - days * 24L * 60L * 60L * 1000L
        entries.removeIf { it.timestamp < cutoff }
    }
}
