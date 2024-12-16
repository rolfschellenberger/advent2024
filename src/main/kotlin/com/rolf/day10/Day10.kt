package com.rolf.day10

import com.rolf.Day
import com.rolf.util.Matrix
import com.rolf.util.MatrixInt
import com.rolf.util.Path
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val map = MatrixInt.build(splitLines(lines))
        val startLocations = map.find(0)
        val endLocations = map.find(9).toSet()

        // Find the path from the 0 to a 9, increasing every step with 1
        var sum = 0
        for (startLocation in startLocations) {
            for (endLocation in endLocations) {
                val path = map.findPath(
                    startLocation, endLocation, customAllowedFunction = this::allowIncreaseByOne
                )
                if (path.isNotEmpty()) {
                    sum++
                }
            }
        }
        println(sum)
    }

    override fun solve2(lines: List<String>) {
        val map = MatrixInt.build(splitLines(lines))

        // Find all 0 locations on the map
        val startLocations = map.find(0)
        val endLocations = map.find(9).toSet()

        // Find all paths from the 0 to a 9, increasing every step with 1
        var sum = 0
        for (startLocation in startLocations) {
            for (endLocation in endLocations) {
                val paths = map.findAllPaths(
                    startLocation, endLocation, customAllowedFunction = this::allowIncreaseByOne
                )
                sum += paths.size
            }
        }
        println(sum)
    }

    fun allowIncreaseByOne(grid: Matrix<Int>, from: Point, to: Point, path: Path): Boolean {
        return grid.get(from) + 1 == grid.get(to)
    }
}
