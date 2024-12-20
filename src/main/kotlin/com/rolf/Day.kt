package com.rolf

import com.rolf.util.getNumbers
import com.rolf.util.readLines
import com.rolf.util.removeLastEmptyLine
import kotlin.time.measureTime

abstract class Day {
    private val day = javaClass.packageName.getNumbers()

    init {
        println("+--------+")
        println("| Day $day |")
        println("+--------+")
    }

    fun run() {
        if (testRun()) {
            runPart("Test 1", "/$day-test.txt", ::solve1)
        }
        if (realRun()) {
            runPart("Part 1", "/$day.txt", ::solve1)
        }
        println("------------------------------------------------")
        if (testRun()) {
            runPart("Test 2", "/$day-test.txt", ::solve2)
        }
        if (realRun()) {
            runPart("Part 2", "/$day.txt", ::solve2)
        }
    }

    fun runPart(title: String, fileName: String, function: (List<String>) -> Unit) {
        println("-- $title | $fileName --")
        val time = measureTime {
            function(removeLastEmptyLine(readLines(fileName)))
        }
        println("-- $time --")
    }

    protected open fun testRun(): Boolean = true
    protected open fun realRun(): Boolean = true
    abstract fun solve1(lines: List<String>)
    abstract fun solve2(lines: List<String>)
}
