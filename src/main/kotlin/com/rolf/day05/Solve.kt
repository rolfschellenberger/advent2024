package com.rolf.day05

import com.rolf.Day
import com.rolf.util.groupLines
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val rules = parseRules(lines)
        val updates = parseUpdates(lines)

        val sum = updates.filter {
            isValid(rules, it)
        }.sumOf {
            it[it.size / 2]
        }
        println(sum)
    }

    override fun solve2(lines: List<String>) {
        val rules = parseRules(lines)
        val updates = parseUpdates(lines)

        // Sort the updates by the rules
        val sum = updates.filterNot { update ->
            isValid(rules, update)
        }.map { update ->
            sortUpdate(rules, update)
        }.sumOf {
            it[it.size / 2]
        }
        println(sum)
    }

    private fun parseRules(strings: List<String>): List<String> {
        val (rules, _) = groupLines(strings, "")
        return rules
    }

    private fun parseUpdates(strings: List<String>): List<List<Int>> {
        val (_, updates) = groupLines(strings, "")
        return updates.map {
            splitLine(it, ",").map { it.toInt() }
        }
    }

    private fun isValid(
        rules: List<String>,
        update: List<Int>,
    ): Boolean {
        val sorted = sortUpdate(rules, update)
        return sorted == update
    }

    private fun sortUpdate(rules: List<String>, update: List<Int>): List<Int> {
        return update.sortedWith { o1, o2 ->
            val pair = "$o1|$o2"
            when (rules.contains(pair)) {
                false -> -1
                true -> 0
            }
        }.reversed()
    }
}
