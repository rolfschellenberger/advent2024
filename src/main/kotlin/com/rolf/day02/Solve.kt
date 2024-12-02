package com.rolf.day02

import com.rolf.Day
import com.rolf.util.splitLine
import kotlin.math.absoluteValue

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val reports = toReports(lines).filter {
            isSafe(it)
        }
        println(reports.size)
    }

    private fun toReports(lines: List<String>): List<List<Int>> {
        return lines.map {
            splitLine(it, " ")
                .map { it.toInt() }
        }
    }

    private fun isSafe(report: List<Int>): Boolean {
        val deltas = toDelta(report)
        val allPositive = deltas.all {
            it > 0
        }
        val allNegative = deltas.all {
            it < 0
        }
        val allInRange = deltas.all {
            it.absoluteValue in 1..3
        }
        return (allPositive || allNegative) && allInRange
    }

    private fun toDelta(numbers: List<Int>): List<Int> {
        val deltas = mutableListOf<Int>()
        for (i in 0 until numbers.size - 1) {
            val a = numbers[i]
            val b = numbers[i + 1]
            val delta = b - a
            deltas.add(delta)
        }
        return deltas
    }

    override fun solve2(lines: List<String>) {
        val reports = toReports(lines)
        val alternativeReports = toAlternativeReports(reports)
        val safeReports = alternativeReports.filter {
            it.any {
                isSafe(it)
            }
        }
        println(safeReports.size)
    }

    private fun toAlternativeReports(reports: List<List<Int>>): List<List<List<Int>>> {
        val alternatives = mutableListOf<List<List<Int>>>()
        for (report in reports) {
            val originalAndCombinations = listOf(report) + combinations(report)
            alternatives.add(originalAndCombinations)
        }
        return alternatives
    }

    private fun combinations(ints: List<Int>): List<List<Int>> {
        val combinations = mutableListOf<List<Int>>()
        for (i in 0 until ints.size) {
            val copy = ints.toMutableList()
            copy.removeAt(i)
            combinations.add(copy)
        }
        return combinations
    }
}
