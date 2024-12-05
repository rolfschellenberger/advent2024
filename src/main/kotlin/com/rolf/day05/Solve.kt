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

        var sum = 0
        // Create pairs of each update
        for (update in updates) {
            if (isValid(rules, update)) {
                val center = update[update.size / 2]
                sum += center
            }
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
        val pairs = createPairs(update)
        // Check if all pairs appear in the rules
        return rules.containsAll(pairs)
    }

    private fun createPairs(update: List<Int>): List<String> {
        val pairs = mutableListOf<String>()
        for (i in update.indices) {
            val first = update[i]
            // Create pairs with the numbers after the index
            val others = update.subList(i + 1, update.size)
            for (j in others.indices) {
                val second = others[j]
                pairs.add("$first|$second")
            }
        }
        return pairs
    }

    override fun solve2(lines: List<String>) {
        val rules = parseRules(lines)
        val updates = parseUpdates(lines)

        val incorrectPairs = getIncorrectUpdates(rules, updates)

        var sum = 0
        for (pair in incorrectPairs) {
            // Find the right order
            val ordered = findRightOrder(rules, pair)
            sum += ordered[ordered.size / 2]
        }
        println(sum)
    }

    private fun findRightOrder(
        rules: List<String>,
        update: List<Int>,
    ): List<Int> {
        // Iterate the numbers in update and swap them around when a rule says so
        val ordered = update.toMutableList()
        var changed = true
        while (changed) {
            changed = false
            for (i in ordered.indices) {
                val first = ordered[i]
                for (j in i + 1 until ordered.size) {
                    val second = ordered[j]
                    val pair = "$first|$second"
                    if (!rules.contains(pair)) {
                        // Swap the numbers
                        val temp = ordered[i]
                        ordered[i] = ordered[j]
                        ordered[j] = temp
                        changed = true
                        break
                    }
                }
            }
        }
        return ordered
    }

    private fun getIncorrectUpdates(
        rules: List<String>,
        updates: List<List<Int>>,
    ): List<List<Int>> {
        return updates.filterNot {
            isValid(rules, it)
        }
    }
}
