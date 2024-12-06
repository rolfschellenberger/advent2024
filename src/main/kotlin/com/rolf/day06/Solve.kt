package com.rolf.day06

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val (_, locations) = isInfiniteLoop(matrix)
        println(locations.map { it.first }.toSet().size)
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val (_, locations) = isInfiniteLoop(matrix)

        var count = 0
        for (point in locations.map { it.first }.toSet()) {
            val copy = matrix.copy()
            if (copy.get(point) == "^") continue
            copy.set(point, "#")

            val (infinite, _) = isInfiniteLoop(copy)
            if (infinite) count++
        }
        println(count)
    }

    private fun isInfiniteLoop(matrix: MatrixString): Pair<Boolean, Set<Pair<Point, Direction>>> {
        var guard = matrix.find("^").first()

        val locations = mutableSetOf<Pair<Point, Direction>>()
        var direction = Direction.NORTH
        locations.add(guard to direction)

        while (!matrix.isOutside(guard)) {
            val next = matrix.getForward(guard, direction)
            if (next == null) break
            val nextValue = matrix.get(next)
            if (nextValue == "#") {
                direction = direction.right()
            } else {
                guard = next
                if (!locations.add(next to direction)) {
                    return true to locations
                }
            }
        }
        return false to locations
    }
}
