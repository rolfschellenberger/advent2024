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
        runPart("Test 1", "/$day-test.txt", ::solve1)
        runPart("Part 1", "/$day.txt", ::solve1)
        println("------------------------------------------------")
        runPart("Test 2", "/$day-test.txt", ::solve2)
        runPart("Part 2", "/$day.txt", ::solve2)
    }

    fun runPart(title: String, fileName: String, function: (List<String>) -> Unit) {
        println("-- $title | $fileName --")
        val time = measureTime {
            function(removeLastEmptyLine(readLines(fileName)))
        }.inWholeMilliseconds
        println("-- ${time}ms --")
    }

    abstract fun solve1(lines: List<String>)
    abstract fun solve2(lines: List<String>)
}
