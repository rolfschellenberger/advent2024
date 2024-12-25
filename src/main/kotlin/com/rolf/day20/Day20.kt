package com.rolf.day20

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        println(
            findPaths(lines, 2, 100)
        )
    }

    override fun solve2(lines: List<String>) {
        println(
            findPaths(lines, 20, 100)
        )
    }

    private fun findPaths(lines: List<String>, cheatSteps: Int, minSaveTime: Int): Int {
        val matrix = MatrixString.build(splitLines(lines))
        val start = matrix.find("S").first()
        val end = matrix.find("E").first()
        val nowAllowed = matrix.find("#").toSet()
        val basePath = matrix.findPath(start, end, nowAllowed)
        val basePathLength = basePath.size - 1

        // Since there is 1 path from start till end, we only need to travel from every location on the path to any other
        // location in the future to see if this is a valid cheat location.
        var count = 0
        for ((i, start) in basePath.locations.withIndex()) {
            for (end in basePath.locations.subList(i + 1, basePath.locations.size)) {
                // Skip when the cheat distance is too large
                val cheatDistance = start.distance(end)
                if (cheatDistance > cheatSteps) continue

                // Now the distance is i + cheatDistance + end->finish
                val lastPathLength = basePathLength - basePath.locations.indexOf(end)
                val distance = i + cheatDistance + lastPathLength
                val timeSaved = basePathLength - distance
                if (timeSaved >= minSaveTime) {
                    count++
                }
            }
        }
        return count
    }
}
