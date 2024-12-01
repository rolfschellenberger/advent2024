package com.rolf.day01

import com.rolf.Day
import com.rolf.util.splitLine
import kotlin.math.absoluteValue

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val left = leftList(lines).sorted()
        val right = rightList(lines).sorted()

        println(
            left.zip(right).sumOf { (l, r) ->
                (l - r).absoluteValue
            }
        )
    }

    override fun solve2(lines: List<String>) {
        val left = leftList(lines)
        val right = rightList(lines)

        println(
            left.sumOf { l ->
                l * right.count { it == l }
            }
        )
    }

    fun leftList(lines: List<String>): List<Int> {
        return lines.map { line ->
            splitLine(line, " ").first().toInt()
        }
    }

    fun rightList(lines: List<String>): List<Int> {
        return lines.map { line ->
            splitLine(line, " ").last().toInt()
        }
    }
}
