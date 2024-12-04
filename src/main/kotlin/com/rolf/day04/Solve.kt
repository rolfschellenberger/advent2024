package com.rolf.day04

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))

        // Rotate the matrix to have 4 versions of it
        val degree90 = matrix.copy()
        degree90.rotateRight()
        val degree180 = degree90.copy()
        degree180.rotateRight()
        val degree270 = degree180.copy()
        degree270.rotateRight()

        val xmasCounts = countXMAS(matrix) + countXMAS(degree90) + countXMAS(degree180) + countXMAS(degree270)
        println(xmasCounts)
    }

    private fun countXMAS(matrix: MatrixString): Int {
        // Now iterate the matrix from all entry points to construct the strings on each path.
        // On every entry point, we can iterate horizontally, vertically and diagonally.
        val borderPoints = matrix.allPoints().filter {
            it.y == 0
        }

        // For every entry, iterate 3 ways down: in a straight line and diagonally left and right
        val lines = mutableListOf<String>()
        for (entry in borderPoints) {
            // y++
            val down = mutableListOf<String>()
            for (y in entry.y until matrix.height()) {
                val value = matrix.get(entry.x, y)
                down.add(value)
            }
            lines.add(down.joinToString(""))

            // y++ and x--
            val downLeft = mutableListOf<String>()
            var xDown = entry.x
            for (y in entry.y until matrix.height()) {
                // The last x position is not valid (because it overlaps with another path)
                if (entry.x == matrix.width() - 1) {
                    break
                }
                if (xDown < 0) {
                    break
                }
                val value = matrix.get(xDown--, y)
                downLeft.add(value)
            }
            lines.add(downLeft.joinToString(""))

            // y++ and x++
            val downRight = mutableListOf<String>()
            xDown = entry.x
            for (y in entry.y until matrix.height()) {
                if (xDown >= matrix.width()) {
                    break
                }
                val value = matrix.get(xDown++, y)
                downRight.add(value)
            }
            lines.add(downRight.joinToString(""))
        }

        return lines.sumOf {
            countXMAS(it)
        }
    }

    private fun countXMAS(line: String): Int {
        return line.windowed(4).count { it == "XMAS" }
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))

        var count = 0
        for (point in matrix.allPoints()) {
            val value = matrix.get(point)
            // Check we are the center of the XMAS
            if (value == "A") {
                // Check the 4 directions
                if (point.x < 1 || point.x >= matrix.width() - 1
                    || point.y < 1 || point.y >= matrix.height() - 1
                ) {
                    continue
                }
                val topRight = matrix.get(point.x + 1, point.y - 1)
                val topLeft = matrix.get(point.x - 1, point.y - 1)
                val bottomRight = matrix.get(point.x + 1, point.y + 1)
                val bottomLeft = matrix.get(point.x - 1, point.y + 1)

                val diagonal1 = "$topLeft$value$bottomRight"
                val diagonal2 = "$topRight$value$bottomLeft"
                if (diagonal1 == "MAS" || diagonal1 == "SAM") {
                    if (diagonal2 == "MAS" || diagonal2 == "SAM") {
                        count++
                    }
                }
            }
        }
        println(count)
    }
}
