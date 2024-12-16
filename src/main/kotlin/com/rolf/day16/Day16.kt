package com.rolf.day16

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val maze = MatrixString.build(splitLines(lines))
        val start = maze.find("S").first()
        val end = maze.find("E").first()
        val notAllowed = maze.find("#").toSet()

        val path = maze.findPath(
            start,
            end,
            notAllowed,
            false,
            customScoreFunction = this::customScoreFunction
        )
        println(path.score)
    }

    override fun solve2(lines: List<String>) {
        val maze = MatrixString.build(splitLines(lines))
        val start = maze.find("S").first()
        val end = maze.find("E").first()
        val notAllowed = maze.find("#").toSet()
        val path = maze.findPath(
            start,
            end,
            notAllowed,
            false,
            customScoreFunction = this::customScoreFunction
        )
        val optimalScore = path.score

        // Now try to find alternative paths with the same score not allowing it to travel any path of the first found path
        val visited = mutableSetOf<Path>(path)
        val excluded = path.locations.toMutableSet()
        while (excluded.isNotEmpty()) {
            val next = excluded.first()
            excluded.remove(next)

            val path =
                maze.findPath(start, end, notAllowed + next, false, customScoreFunction = this::customScoreFunction)
            if (path.score == optimalScore) {
                if (visited.add(path)) {
                    excluded.addAll(path.locations)
                }
            }
        }

        val uniqueLocations = visited.flatMap { path -> path.locations }.toSet()
        println(uniqueLocations.size)
    }

    fun customScoreFunction(grid: Matrix<String>, from: Point, to: Point, path: Path): Int {
        // When there is no turn, the score is 1
        var previous: Point? = if (path.size > 1) path.locations[path.locations.size - 2] else null
        if (previous == null) {
            val direction = directionBetween(from, to)
            if (direction == Direction.NORTH || direction == Direction.SOUTH) return 1000 + 1
            if (direction == Direction.EAST) return 1
            return 1000 + 1
        }
        val direction1 = directionBetween(previous, from)
        val direction2 = directionBetween(from, to)
        if (direction1 == direction2) return 1
        return 1000 + 1
    }

    private fun directionBetween(from: Point, to: Point): Direction {
        val xDiff = from.x - to.x
        val yDiff = from.y - to.y
        if (xDiff < 0) return Direction.EAST
        if (xDiff > 0) return Direction.WEST
        if (yDiff < 0) return Direction.SOUTH
        return Direction.NORTH
    }
}
