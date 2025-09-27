package com.frontlinehelper.app.network

import com.frontlinehelper.app.data.AuditLogEntry
import com.frontlinehelper.app.data.AuditLogRepository
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object FrontlineService {
    data class FetchResult(val html: String?, val retryAfterSeconds: Long?)

    fun fetchJobs(urlString: String, authHeader: String? = null): FetchResult {
        AuditLogRepository.write(AuditLogEntry(UUID.randomUUID().toString(), actionType = "fetchJobs", details = urlString))

        val url = URL(urlString)
        val conn = (url.openConnection() as HttpURLConnection)
        try {
            conn.requestMethod = "GET"
            if (!authHeader.isNullOrBlank()) conn.setRequestProperty("Authorization", authHeader)
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000

            val code = conn.responseCode
            val retryAfter = conn.getHeaderField("Retry-After")?.toLongOrNull()

            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val reader = BufferedReader(InputStreamReader(stream))
            val sb = StringBuilder()
            reader.forEachLine { sb.append(it).append('\n') }

            return FetchResult(sb.toString(), retryAfter)
        } finally {
            conn.disconnect()
        }
    }
}
