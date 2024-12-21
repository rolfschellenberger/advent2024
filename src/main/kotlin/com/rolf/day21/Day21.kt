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
        return false
    }

    override fun solve1(lines: List<String>) {
        val codes = lines

        val numPad = """
            789
            456
            123
             0A
             """.trimIndent().lines()
        val numericKeypad = MatrixString.build(splitLines(numPad))
        val notAllowedNumericKeypad = numericKeypad.find(" ").toSet()
        val startNumericKeypad = numericKeypad.find("A").first()

        val dirPad = """
             ^A
            <v>
             """.trimIndent().lines()
        val directionalKeypad = MatrixString.build(splitLines(dirPad))
        val notAllowedDirectionalKeypad = directionalKeypad.find(" ").toSet()
        val startDirectionalKeypad = directionalKeypad.find("A").first()

        // For the directional keypad, we need to find the shortest sequence for every position to any other position
        // We can cache the shortest sequence for every position to any other position
        for (pointA in directionalKeypad.allPoints() - notAllowedDirectionalKeypad) {
            for (pointB in directionalKeypad.allPoints() - notAllowedDirectionalKeypad) {
                val sequences = findShortestSequences(directionalKeypad, pointA, pointB, notAllowedDirectionalKeypad)

            }
//            findAllPathCombinations(directionalKeypad, listOf("A"), point, notAllowedDirectionalKeypad, useCache = true)
        }
//        return

        var totalComplexity = 0L
        for (code in codes) {
            val codePattern = code.map { it.toString() }
            println(codePattern.joinToString(""))

            val pattern1 =
                findAllPathCombinations(numericKeypad, codePattern, startNumericKeypad, notAllowedNumericKeypad)
                    .map { path ->
                        path.flatMap { toSequence(it) }
                    }
//            pattern1.forEach { println(it) }

            var minLength = Int.MAX_VALUE
            val numericPart = code.filter { it.isDigit() }.toInt()


            for (p1 in pattern1) {
//                println(p1)
                val pattern2 =
                    findAllPathCombinations(
                        directionalKeypad,
                        p1,
                        startDirectionalKeypad,
                        notAllowedDirectionalKeypad,
                        useCache = true
                    )
                        .map { path ->
                            path.flatMap { toSequence(it) }
                        }
//                pattern2.forEach { println(it) }

                for (p2 in pattern2) {
//                    println(p2)
                    val pattern3 = findAllPathCombinations(
                        directionalKeypad,
                        p2,
                        startDirectionalKeypad,
                        notAllowedDirectionalKeypad,
                        useCache = true
                    )
                        .map { path ->
                            path.flatMap { toSequence(it) }
                        }
//                    pattern3.forEach { println(it) }
                    val length = pattern3.first().size
                    minLength = minOf(minLength, length)
//                    break
                }
//                break
            }
            val complexity = minLength * numericPart
            println("$code: $minLength * $numericPart = $complexity")
            totalComplexity += complexity
        }
        println(totalComplexity)
    }

    private fun findShortestSequences(
        keypad: MatrixString,
        start: Point,
        end: Point,
        notAllowed: Set<Point>,
    ): List<List<String>> {
        println("Start: $start, End: $end")
        val sequences = keypad.findAllPaths(start, end, notAllowed)
            .map { path ->
                toSequence(path)
            }

        val shortestSequence = sequences.minByOrNull { it.size }!!
        val shortestSequences = sequences.filter { it.size == shortestSequence.size }
        if (shortestSequences.size > 1) {
            shortestSequences.forEach { println(it) }
            println()
        }
        return shortestSequences
    }

    var cache: MutableMap<Pair<Point, String>, List<List<Path>>> = mutableMapOf()

    private fun findAllPathCombinations(
        keypad: MatrixString,
        pattern: List<String>,
        startPoint: Point,
        notAllowed: Set<Point>,
        traveled: List<Path> = emptyList(),
        useCache: Boolean = false,
    ): List<List<Path>> {
        if (pattern.isEmpty()) return listOf(traveled)

        val firstChar = pattern.first()
        val key = Pair(startPoint, pattern.joinToString(""))
//        println(key)
        if (useCache && cache.containsKey(key)) {
            return cache[key]!!
        }

        // We need to find all possible (shortest) paths to the next character
        val paths = keypad.findAllPaths(startPoint, keypad.find(firstChar).first(), notAllowed, diagonal = false)
        val shortestPath = paths.minByOrNull { it.locations.size }!!
        val shortestPaths = paths.filter { it.locations.size == shortestPath.locations.size }

        // For every path, we need to travel to the next character
        val result = mutableListOf<List<Path>>()
        for (path in shortestPaths) {
            val nextPaths = findAllPathCombinations(
                keypad,
                pattern.drop(1),
                path.locations.last(),
                notAllowed,
                traveled + path,
            )
            result.addAll(nextPaths)
        }
        if (useCache) {
            cache[key] = result
        }
        return result
    }

    private fun toSequence(path: Path): List<String> {
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
        return sequence + "A"
    }

    override fun solve2(lines: List<String>) {
    }

//    // Directions and their effects on position
//    data class Direction(val dx: Int, val dy: Int, val symbol: Char)
//
//    // Define movements for the numeric keypad and directional keypad
//    val movements = listOf(
//        Direction(0, -1, '^'),  // Up
//        Direction(0, 1, 'v'),   // Down
//        Direction(-1, 0, '<'),  // Left
//        Direction(1, 0, '>')    // Right
//    )
//
//    // Numeric keypad layout
//    val numericKeypad = mapOf(
//        Pair(0, 0) to '7', Pair(1, 0) to '8', Pair(2, 0) to '9',
//        Pair(0, 1) to '4', Pair(1, 1) to '5', Pair(2, 1) to '6',
//        Pair(0, 2) to '1', Pair(1, 2) to '2', Pair(2, 2) to '3',
//        Pair(1, 3) to '0', Pair(2, 3) to 'A'
//    )
//
//    fun bfs(start: Pair<Int, Int>, target: Char): Pair<String, Int>? {
//        val queue: Queue<Triple<Pair<Int, Int>, String, Int>> = LinkedList()
//        queue.add(Triple(start, "", 0))
//        val visited = mutableSetOf<Pair<Int, Int>>()
//
//        while (queue.isNotEmpty()) {
//            val (current, path, steps) = queue.poll()
//            if (current !in numericKeypad.keys || current in visited) continue
//            visited.add(current)
//
//            // If target is reached, return path and steps
//            if (numericKeypad[current] == target) return Pair(path, steps)
//
//            for (move in movements) {
//                val next = Pair(current.first + move.dx, current.second + move.dy)
//                queue.add(Triple(next, path + move.symbol, steps + 1))
//            }
//        }
//        return null
//    }
//
//    fun calculateShortestSequence(code: String): Pair<String, Int> {
//        var currentPos = Pair(2, 3) // Start at 'A'
//        var totalSteps = 0
//        val pathBuilder = StringBuilder()
//
//        for (digit in code) {
//            val (path, steps) = bfs(currentPos, digit) ?: throw IllegalStateException("Unreachable digit: $digit")
//            pathBuilder.append(path).append('A') // Append path and activation ('A')
//            totalSteps += steps + 1 // +1 for 'A'
//            currentPos = numericKeypad.entries.first { it.value == digit }.key
//        }
//        return Pair(pathBuilder.toString(), totalSteps)
//    }
//
//    fun main() {
//        val codes = listOf("029A", "980A", "179A", "456A", "379A")
//        var totalComplexity = 0
//
//        for (code in codes) {
//            val (sequence, steps) = calculateShortestSequence(code)
//            val numericValue = code.filter { it.isDigit() }.toInt()
//            val complexity = steps * numericValue
//            totalComplexity += complexity
//
//            println("Code: $code, Sequence: $sequence, Steps: $steps, Numeric Value: $numericValue, Complexity: $complexity")
//        }
//
//        println("Total Complexity: $totalComplexity")
//    }
}
