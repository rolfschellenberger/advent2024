package com.rolf.day11

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        var stones = parseStones(lines.first())
        repeat(25) {
            stones = stones.flatMap { it.evolve() }
        }
        println(stones.size)
    }

    private fun parseStones(string: String): List<Stone> {
        return string.split(" ").map { Stone(it.toLong()) }
    }

    override fun solve2(lines: List<String>) {
        var stones = parseStones(lines.first())

        // Sum up each stone separately to optimize with cache
        println(
            stones.sumOf {
                solve(it, 75)
            }
        )
    }

    val cache = mutableMapOf<Pair<Long, Int>, Long>()

    fun solve(stone: Stone, steps: Int): Long {
        // Cache results
        if (cache.containsKey(Pair(stone.value, steps))) {
            return cache[Pair(stone.value, steps)]!!
        }

        // No more steps to take, the result is 1 stone
        if (steps == 0) return 1

        // Sum up the evolved stones
        val sum = stone.evolve().sumOf {
            solve(it, steps - 1)
        }

        // Cache the result before returning
        cache[Pair(stone.value, steps)] = sum
        return sum
    }
}
