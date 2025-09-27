package com.frontlinehelper.app.scheduling

import com.frontlinehelper.app.network.FrontlineService
import com.frontlinehelper.app.parser.JobParser
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object PollingScheduler {
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var currentTask: java.util.concurrent.ScheduledFuture<*>? = null

    fun start(url: String, initialIntervalSeconds: Long = 60) {
        stop()
        currentTask = executor.scheduleWithFixedDelay({
            try {
                val result = FrontlineService.fetchJobs(url)
                val html = result.html
                val retry = result.retryAfterSeconds
                if (html != null) {
                    val jobs = JobParser.parse(html)
                    // TODO: apply filters and notify UI via app-level observer
                }
                if (retry != null) {
                    // if provider suggests Retry-After, reschedule with that interval
                    reschedule(retry)
                }
            } catch (e: Exception) {
                // log and continue
            }
        }, 0, initialIntervalSeconds, TimeUnit.SECONDS)
    }

    private fun reschedule(seconds: Long) {
        stop()
        currentTask = executor.scheduleWithFixedDelay({}, seconds, seconds, TimeUnit.SECONDS)
    }

    fun stop() {
        currentTask?.cancel(true)
        currentTask = null
    }
}
