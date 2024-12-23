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
    override fun solve1(lines: List<String>) {
        println(solve(lines, 2))
    }

    override fun solve2(lines: List<String>) {
        println(solve(lines, 25))
    }

    private fun solve(lines: List<String>, depth: Int): Long {
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
            var next = getNextCodes(code, numericPossiblePaths)

            var minLength = Long.MAX_VALUE
            for (n in next) {
                var length = 0L
                for ((a, b) in getAllPairs(n)) {
                    length += shortestLength(a, b, depth, directionalPossiblePaths)
                }
                minLength = minOf(minLength, length)
            }

            val complexity = minLength * code.filter { it.isDigit() }.toInt()
            totalComplexity += complexity
        }
        return totalComplexity
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
        for ((from, to) in getAllPairs(code)) {
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

    private fun getAllPairs(code: String): List<Pair<String, String>> {
        return "A$code".zipWithNext().map {
            it.first.toString() to it.second.toString()
        }
    }

    private fun shortestLength(
        from: String,
        to: String,
        depth: Int,
        possiblePaths: MutableMap<Pair<String, String>, List<String>>,
        cache: MutableMap<String, Long> = mutableMapOf(),
    ): Long {
        val key = "$from-$to-$depth"
        cache[key]?.let {
            return it
        }

        if (depth == 0) {
            return 0
            throw IllegalArgumentException("Depth must be at least 1")
        }

        var minLength = Long.MAX_VALUE
        val nextPatterns = possiblePaths[Pair(from.toString(), to.toString())] ?: error("No sequences found")
        if (depth == 1) {
            return nextPatterns[0].length.toLong()
        }

        for (nextPattern in nextPatterns) {
            var length = 0L
            for ((a, b) in getAllPairs(nextPattern)) {
                length += shortestLength(a.toString(), b.toString(), depth - 1, possiblePaths, cache)
            }
            minLength = minOf(minLength, length)
        }

        cache[key] = minLength
        return minLength
    }
}
