package com.rolf.day21

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Path
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        val numPad = """
            789
            456
            123
             0A
             """.trimIndent().lines()
        val numericKeypad = MatrixString.build(splitLines(numPad))
        val notAllowedNumericKeypad = numericKeypad.find(" ").toSet()

        val dirPad = """
             ^A
            <v>
             """.trimIndent().lines()
        val directionalKeypad = MatrixString.build(splitLines(dirPad))
        val notAllowedDirectionalKeypad = directionalKeypad.find(" ").toSet()

        // Calculate the possible shortest paths between two points on every keypad
        val numericPossiblePaths = mutableMapOf<Pair<String, String>, List<String>>()
        for (pointA in numericKeypad.allElements() - " ") {
            for (pointB in numericKeypad.allElements() - " ") {
                val shortestPaths = findShortestPaths(numericKeypad, pointA, pointB, notAllowedNumericKeypad)
                val sequences = shortestPaths.map { toSequence(it) }
                numericPossiblePaths[Pair(pointA, pointB)] = sequences.map { it }
            }
        }

        val directionalPossiblePaths = mutableMapOf<Pair<String, String>, List<String>>()
        for (pointA in directionalKeypad.allElements() - " ") {
            for (pointB in directionalKeypad.allElements() - " ") {
                val shortestPaths = findShortestPaths(directionalKeypad, pointA, pointB, notAllowedDirectionalKeypad)
                val sequences = shortestPaths.map { toSequence(it) }
                directionalPossiblePaths[Pair(pointA, pointB)] = sequences.map { it }
            }
        }

        var totalComplexity = 0L
        for (code in lines) {
            println(code)
            var next = getNextCodes(code, numericPossiblePaths)

            repeat(2) {
                val allNext = mutableListOf<String>()
                for (n in next) {
                    val possibleNext = getNextCodes(n, directionalPossiblePaths)
                    allNext.addAll(possibleNext)
                }
                next = allNext
            }

            val minLength = next.minOf { it.length }
            val numericPart = code.filter { it.isDigit() }.toInt()
            val complexity = minLength * numericPart
            totalComplexity += complexity
        }
        println(totalComplexity)
    }

    private fun findShortestPaths(
        keypad: MatrixString,
        from: String,
        to: String,
        notAllowed: Set<Point>,
    ): List<Path> {
        val start = keypad.find(from).first()
        val end = keypad.find(to).first()
        val paths = keypad.findAllPaths(start, end, notAllowed)
        val shortestPathDistance = paths.minOfOrNull { it.locations.size }
        return paths.filter { it.locations.size == shortestPathDistance }
    }

    private fun toSequence(path: Path): String {
        val sequence = mutableListOf<String>()
        for ((a, b) in path.locations.zipWithNext()) {
            if (a.x < b.x) {
                sequence.add(">")
            }
            if (a.x > b.x) {
                sequence.add("<")
            }
            if (a.y < b.y) {
                sequence.add("v")
            }
            if (a.y > b.y) {
                sequence.add("^")
            }
        }
        return (sequence + "A").joinToString("")
    }

    private fun getNextCodes(
        code: String,
        possiblePaths: MutableMap<Pair<String, String>, List<String>>,
    ): List<String> {
        val sequences = mutableListOf<List<String>>()
        for ((from, to) in "A$code".zipWithNext()) {
            val possiblePaths = possiblePaths[Pair(from.toString(), to.toString())] ?: error("No sequences found")
            sequences.add(possiblePaths)
        }

        // We need to return all possible combinations of sequences
        return generateCombinations(sequences).map { it.joinToString("") }
    }

    fun generateCombinations(input: List<List<String>>): List<List<String>> {
        if (input.isEmpty()) return listOf(emptyList())

        val firstList = input.first()
        val restCombinations = generateCombinations(input.drop(1))

        val result = mutableListOf<List<String>>()
        for (element in firstList) {
            for (combination in restCombinations) {
                result.add(listOf(element) + combination)
            }
        }
        return result
    }

    override fun solve2(lines: List<String>) {
        val numPad = """
            789
            456
            123
             0A
             """.trimIndent().lines()
        val numericKeypad = MatrixString.build(splitLines(numPad))
        val notAllowedNumericKeypad = numericKeypad.find(" ").toSet()

        val dirPad = """
             ^A
            <v>
             """.trimIndent().lines()
        val directionalKeypad = MatrixString.build(splitLines(dirPad))
        val notAllowedDirectionalKeypad = directionalKeypad.find(" ").toSet()

        // Calculate the possible shortest paths between two points on every keypad
        val numericPossiblePaths = mutableMapOf<Pair<String, String>, List<String>>()
        for (pointA in numericKeypad.allElements() - " ") {
            for (pointB in numericKeypad.allElements() - " ") {
                val shortestPaths = findShortestPaths(numericKeypad, pointA, pointB, notAllowedNumericKeypad)
                val sequences = shortestPaths.map { toSequence(it) }
                numericPossiblePaths[Pair(pointA, pointB)] = sequences.map { it }
            }
        }

        val directionalPossiblePaths = mutableMapOf<Pair<String, String>, List<String>>()
        for (pointA in directionalKeypad.allElements() - " ") {
            for (pointB in directionalKeypad.allElements() - " ") {
                val shortestPaths = findShortestPaths(directionalKeypad, pointA, pointB, notAllowedDirectionalKeypad)
                val sequences = shortestPaths.map { toSequence(it) }
                directionalPossiblePaths[Pair(pointA, pointB)] = sequences.map { it }
            }
        }

        var totalComplexity = 0L
        for (code in lines) {
            println(code)
            var next = getNextCodes(code, numericPossiblePaths)

            repeat(25) {
                val allNext = mutableListOf<String>()
                for (n in next) {
                    val possibleNext = getNextCodes(n, directionalPossiblePaths)
                    allNext.addAll(possibleNext)
                }
                next = allNext
            }

            val minLength = next.minOf { it.length }
            val numericPart = code.filter { it.isDigit() }.toInt()
            val complexity = minLength * numericPart
            totalComplexity += complexity
        }
        println(totalComplexity)
    }
}
