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
