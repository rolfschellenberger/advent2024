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
        val guardLocations = locations.map { it.first }.toSet()
        println(guardLocations.size)
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val (_, locations) = isInfiniteLoop(matrix)
        val guardLocations = locations.map { it.first }.toSet()

        var count = 0
        for (point in guardLocations) {
            val copy = placeObstruction(matrix, point)
            count += if (isInfiniteLoop(copy).first) 1 else 0
        }
        println(count)
    }

    private fun placeObstruction(matrix: MatrixString, point: Point): MatrixString {
        val copy = matrix.copy()
        if (copy.get(point) != "^") {
            copy.set(point, "#")
        }
        return copy
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
