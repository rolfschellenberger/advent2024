package com.rolf.day19

import com.rolf.Day
import com.rolf.util.groupLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        val (towels, designs) = parse(lines)

        println(
            designs
                .map { isPossible(towels, it) }
                .count { it > 0 }
        )
    }

    override fun solve2(lines: List<String>) {
        val (towels, designs) = parse(lines)

        println(
            designs
                .sumOf { isPossible(towels, it) }
        )
    }

    private fun parse(lines: List<String>): Pair<List<String>, List<String>> {
        val (t, designs) = groupLines(lines, "")
        val towels = t.first().split(", ")
        return towels to designs
    }

    fun isPossible(towels: List<String>, design: String): Long {
        return isPossibleWithCache(towels, design, mutableMapOf<String, Long>())
    }

    fun isPossibleWithCache(towels: List<String>, design: String, cache: MutableMap<String, Long>): Long {
        if (design in cache) return cache[design]!!
        var count = 0L

        // Found a match!
        if (design.isEmpty()) count = 1

        // Try each towel to match the design
        for (towel in towels) {
            if (design.startsWith(towel)) {
                val remaining = design.removePrefix(towel)
                count += isPossibleWithCache(towels, remaining, cache)
            }
        }

        // Cache to optimize in next iterations!
        cache[design] = count
        return count
    }
}
