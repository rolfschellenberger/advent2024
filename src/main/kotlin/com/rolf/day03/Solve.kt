package com.rolf.day03

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val line = lines.joinToString("")
        val regex = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""")
        val matches = regex.findAll(line).map { it.value }
        val outcome = matches.map { multiply(it) }
        println(outcome.sum())
    }

    private fun multiply(string: String): Int {
        val numbers = string.split(",").map { it.replace("mul(", "").replace(")", "").toInt() }
        return numbers[0] * numbers[1]
    }

    override fun solve2(lines: List<String>) {
        val line = lines.joinToString("")
        println(calculate(line))
    }

    private fun calculate(line: String): Long {
        var enabled = true
        var index = 0
        val regex = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""")

        var sum = 0L
        while (index < line.length) {
            val match = regex.find(line, index)
            if (match != null) {
                val before = line.substring(index, match.range.first)
                if (before.contains("don't()")) {
                    enabled = false
                }
                if (before.contains("do()")) {
                    enabled = true
                }

                if (enabled) {
                    sum += multiply(match.value)
                }

                index = match.range.last + 1
            } else {
                break
            }
        }
        return sum
    }
}
