package com.rolf.day08

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.getPermutations
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val antennaPairs = findAntennaPairs(matrix)
        val antiNodeLocations = findAntiNodes(matrix, antennaPairs)
        println(antiNodeLocations.toSet().size)
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val antennaPairs = findAntennaPairs(matrix)
        val antiNodeLocations = findAntiNodes(matrix, antennaPairs, false)
        println(antiNodeLocations.toSet().size)
    }

    private fun findAntennaPairs(matrix: MatrixString): List<Pair<Point, Point>> {
        val uniqueSymbols = matrix.allPoints().map { matrix.get(it) }.distinct().toMutableList()
        return uniqueSymbols.filterNot { it == "." }.map { symbol ->
            matrix.find(symbol)
        }.map { symbolLocations ->
            // Find all permutations of 2 locations
            getPermutations(symbolLocations, 2).map { permutation ->
                val (a, b) = permutation
                a to b
            }
        }.flatten()
    }

    private fun findAntiNodes(
        matrix: MatrixString,
        antennaPairs: List<Pair<Point, Point>>,
        oneStep: Boolean = true,
    ): List<Point> {
        val antiNodeLocations = mutableListOf<Point>()

        for ((a, b) in antennaPairs) {
            // Add the towers too when taking multiple steps
            if (!oneStep) {
                antiNodeLocations.add(a)
                antiNodeLocations.add(b)
            }

            // Now calculate the distance between the two points
            val deltaX = a.x - b.x
            val deltaY = a.y - b.y

            // And move the same distance in both directions
            var c = Point(a.x + deltaX, a.y + deltaY)
            while (!matrix.isOutside(c)) {
                antiNodeLocations.add(c)
                if (oneStep) {
                    break
                }
                c = c.move(deltaX, deltaY)
            }

            var d = Point(b.x - deltaX, b.y - deltaY)
            while (!matrix.isOutside(d)) {
                antiNodeLocations.add(d)
                if (oneStep) {
                    break
                }
                d = d.move(-deltaX, -deltaY)
            }
        }
        return antiNodeLocations
    }
}
