package com.frontlinehelper.app.parser

import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class JobParserTest {
    @Test
    fun `parses sample job fixture`() {
        val fixturePath = Paths.get("../../../../../specs/001-title-frontline-job/fixtures/sample-job.html").toAbsolutePath().normalize()
        val html = Files.readString(fixturePath)

        // TODO: implement JobParser.parse(html)
        // For now the test intentionally fails to drive TDD for implementation (T007)
        val parsed = JobParser.parse(html)

        // expected fields from fixture
        Assert.assertEquals(1, parsed.size)
        val job = parsed[0]
        Assert.assertEquals("12345", job.id)
        Assert.assertEquals("Lincoln High School", job.school)
        Assert.assertEquals("2025-10-01", job.date)
        Assert.assertEquals("FULL", job.dayLength)
        Assert.assertEquals("Math 101", job.className)
        Assert.assertEquals("Ms. Smith", job.teacher)
        Assert.assertEquals("Mathematics", job.subject)
    }
}

// Minimal placeholder to allow compilation; will be implemented properly in T007
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
        // intentionally return empty to make the test fail and drive TDD
        return emptyList()
    }
}
