package com.rolf.day04

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))

        // Find the letter 'X' and see if it is part of the word 'XMAS' in any direction (horizontal, vertical, diagonal) and in reverse.
        val xmasCounts = matrix.allPoints().filter {
            matrix.get(it) == "X"
        }.sumOf {
            countXMAS(matrix, it)
        }
        println(xmasCounts)
    }

    private fun countXMAS(matrix: MatrixString, point: Point): Int {
        // Find the next 3 letters in every direction from the starting point
        val wordLeft = getWord(matrix, point, -1, 0)
        val wordRight = getWord(matrix, point, 1, 0)
        val wordUp = getWord(matrix, point, 0, -1)
        val wordDown = getWord(matrix, point, 0, 1)
        val wordUpRight = getWord(matrix, point, 1, -1)
        val wordUpLeft = getWord(matrix, point, -1, -1)
        val wordDownRight = getWord(matrix, point, 1, 1)
        val wordDownLeft = getWord(matrix, point, -1, 1)
        return listOf(
            wordLeft,
            wordRight,
            wordUp,
            wordDown,
            wordUpRight,
            wordUpLeft,
            wordDownRight,
            wordDownLeft
        ).count { it.startsWith("XMAS") }
    }

    private fun getWord(matrix: MatrixString, start: Point, deltaX: Int, deltaY: Int): String {
        val word = StringBuilder()

        var location = start
        while (!matrix.isOutside(location)) {
            word.append(matrix.get(location))
            location = location.move(deltaX, deltaY)
        }
        return word.toString()
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
