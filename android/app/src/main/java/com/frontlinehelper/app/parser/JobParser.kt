package com.frontlinehelper.app.parser

// Simple HTML parser for deterministic fixtures.
// This implementation intentionally avoids external dependencies to keep the scaffold minimal.
object JobParser {
    data class Job(
        val id: String,
        val school: String,
        val date: String,
        val dayLength: String,
        val className: String,
        val teacher: String,
        val subject: String
    )

    fun parse(html: String): List<Job> {
        val jobs = mutableListOf<Job>()

        // Very small, forgiving parser for the fixture HTML structure used in tests.
        // It finds occurrences of <div class="job" data-id="..."> ... </div>
        var index = 0
        while (true) {
            val divStart = html.indexOf("<div class=\"job\"", index)
            if (divStart == -1) break
            val divEnd = html.indexOf("</div>", divStart)
            if (divEnd == -1) break
            val segment = html.substring(divStart, divEnd)

            val id = extractAttribute(segment, "data-id") ?: ""
            val school = extractTagText(segment, "span", "class=\"school\"") ?: ""
            val date = extractTagText(segment, "span", "class=\"date\"") ?: ""
            val dayLength = extractTagText(segment, "span", "class=\"daylength\"") ?: ""
            val className = extractTagText(segment, "span", "class=\"class\"") ?: ""
            val teacher = extractTagText(segment, "span", "class=\"teacher\"") ?: ""
            val subject = extractTagText(segment, "span", "class=\"subject\"") ?: ""

            jobs.add(Job(id, school, date, dayLength, className, teacher, subject))

            index = divEnd + 6
        }

        return jobs
    }

    private fun extractAttribute(segment: String, attr: String): String? {
        val re = Regex("$attr=\\\"([^\\\"]+)\\\"")
        val m = re.find(segment)
        return m?.groups?.get(1)?.value
    }

    private fun extractTagText(segment: String, tag: String, withClass: String): String? {
        // find tag with class marker, then extract inner text
        val idx = segment.indexOf(withClass)
        if (idx == -1) return null
        // find closing '>' from the tag start
        val tagStart = segment.lastIndexOf('<', idx)
        if (tagStart == -1) return null
        val openEnd = segment.indexOf('>', tagStart)
        if (openEnd == -1) return null
        val closeTag = "</$tag>"
        val closeIdx = segment.indexOf(closeTag, openEnd)
        if (closeIdx == -1) return null
        return segment.substring(openEnd + 1, closeIdx).trim()
    }
}
