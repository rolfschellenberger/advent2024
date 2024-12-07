package com.rolf.day07

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val equations = parseEquations(lines)
        println(
            equations.filter {
                it.isValid(listOf("*", "+"))
            }.sumOf {
                it.value
            }
        )
    }

    override fun solve2(lines: List<String>) {
        val equations = parseEquations(lines)
        println(
            equations.filter {
                it.isValid(listOf("*", "+", "||"))
            }.sumOf {
                it.value
            }
        )
    }

    private fun parseEquations(lines: List<String>): List<Equation> {
        return lines.map {
            parseEquation(it)
        }
    }

    private fun parseEquation(line: String): Equation {
        val (value, numbersText) = line.split(": ")
        val numbers = numbersText.split(" ").map { it.toLong() }
        return Equation(value.toLong(), numbers)
    }
}
