package com.rolf.day03

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val line = joinLines(lines)
        val muls = findMuls(line)
        val sum = muls.sumOf {
            multiply(it.value)
        }
        println(sum)
    }

    override fun solve2(lines: List<String>) {
        val line = lines.joinToString("")
        val muls = findMuls(line)
        val dos = findDos(line)
        val donts = findDonts(line)

        val sum = muls.filter { mul ->
            isEnabled(mul, dos, donts)
        }.sumOf {
            multiply(it.value)
        }
        println(sum)
    }

    private fun joinLines(strings: List<String>): String {
        return strings.joinToString("")
    }

    private fun multiply(string: String): Int {
        val numbers = string.split(",").map { it.replace("mul(", "").replace(")", "").toInt() }
        return numbers[0] * numbers[1]
    }

    private fun findMuls(line: String): List<MatchResult> {
        val regex = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""")
        return regex.findAll(line).toList()
    }

    private fun findDos(line: String): Set<Int> {
        val regex = Regex("""do\(\)""")
        return regex.findAll(line).map { it.range.first }.toSet()
    }

    private fun findDonts(line: String): Set<Int> {
        val regex = Regex("""don't\(\)""")
        return regex.findAll(line).map { it.range.first }.toSet()
    }

    private fun isEnabled(
        mul: MatchResult,
        dos: Set<Int>,
        donts: Set<Int>,
    ): Boolean {
        val before = mul.range.first
        val doBefore = dos.filter { it < before }.maxOrNull()
        val dontBefore = donts.filter { it < before }.maxOrNull()

        // No indicator: enabled!
        if (doBefore == null && dontBefore == null) {
            return true
        }

        // Only don't: disabled!
        if (doBefore == null) {
            return false
        }
        // Only do: enabled!
        if (dontBefore == null) {
            return true
        }

        // Enabled when the do is after the don't
        return doBefore > dontBefore
    }
}
