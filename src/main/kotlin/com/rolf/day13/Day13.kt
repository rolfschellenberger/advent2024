package com.rolf.day13

import com.rolf.Day
import com.rolf.util.groupLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val games = groupLines(lines, "").map { parseGame(it) }
        println(games.sumOf {
            it.score()
        })
    }

    override fun solve2(lines: List<String>) {
        val games = groupLines(lines, "").map { parseGame(it) }.map {
            it.copy(px = it.px + 10000000000000L, py = it.py + 10000000000000L)
        }
        println(games.sumOf {
            it.score()
        })
    }

    private fun parseGame(line: List<String>): Game {
        val regex = """(\d+)""".toRegex()
        val values = regex.findAll(line.joinToString(" ")).map {
            it.value.toDouble()
        }.toList()
        return Game(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
            values[5]
        )
    }
}
