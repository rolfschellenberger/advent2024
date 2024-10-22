package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class FilesTest {

    @Test
    fun testReadLines() {
        val lines = readLines("/text.txt")
        assertEquals(9, lines.size)
    }

    @Test
    fun testSplitLine() {
        val input = "a b c def g h ijk"
        assertEquals(17, splitLine(input).size)
        assertEquals(17, splitLine(input, delimiter = "").size)
        assertEquals(7, splitLine(input, delimiter = " ").size)
        assertEquals(1, splitLine(input, delimiter = "  ").size)
        assertEquals(1, splitLine(input, delimiter = "\\s+").size)
        assertEquals(9, splitLine(input, chunkSize = 2).size)
        assertEquals(6, splitLine(input, chunkSize = 3).size)
        assertEquals(1, splitLine(input, chunkSize = 20).size)
        assertEquals(17, splitLine(input, delimiter = "", chunkSize = 0).size)
    }

    @Test
    fun testSplitLinePattern() {
        val input = "a b c def    g h ijk"
        assertEquals(10, splitLine(input, pattern = "\\s".toPattern()).size)
        assertEquals(7, splitLine(input, pattern = "\\s+".toPattern()).size)
    }

    @Test
    fun testSplitLines() {
        val lines = readLines("/bingo.txt")
        val splits1 = splitLines(lines)
        assertEquals(17, splits1.size)
        assertEquals(14, splits1[0].size)
        assertEquals(14, splits1[1].size)
        assertEquals(0, splits1[5].size)
        assertEquals(14, splits1[6].size)

        val splits2 = splitLines(lines, delimiter = " ")
        assertEquals(17, splits2.size)
        assertEquals(5, splits2[0].size)
        assertEquals(5, splits2[1].size)
        assertEquals(1, splits2[5].size)
        assertEquals(6, splits2[6].size)

        val splits3 = splitLines(lines, chunkSize = 5)
        assertEquals(17, splits3.size)
        assertEquals(3, splits3[0].size)
        assertEquals(3, splits3[1].size)
        assertEquals(0, splits3[5].size)
        assertEquals(3, splits3[6].size)

        val splits4 = splitLines(lines, chunkSize = 3).map { line -> line.map { it.trim().toInt() } }
        assertEquals(17, splits4.size)
        assertEquals(83, splits4[0][0])
        assertEquals(4, splits4[6][1])
    }

    @Test
    fun testGroupLines() {
        val lines = readLines("/bingo.txt")
        val groups = groupLines(lines, "")
        assertEquals(3, groups.size)
        assertEquals(5, groups[0].size)
        assertEquals(5, groups[1].size)
        assertEquals(5, groups[2].size)
    }

    @Test
    fun testRemoveLastEmptyLine() {
        val lines = readLines("/text-empty-last-line.txt")
        assertEquals(10, lines.size)
        assertEquals("", lines.last())
        val cleanLines = removeLastEmptyLine(lines)
        assertEquals(9, cleanLines.size)
        assertNotEquals("", cleanLines.last())
    }
}
