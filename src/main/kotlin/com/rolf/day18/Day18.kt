package com.rolf.day18

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val memorySize = if (lines.size > 100) 71 else 7
        val steps = if (lines.size > 100) 1024 else 12

        val memory = MatrixString.buildDefault(memorySize, memorySize, ".")
        for (line in lines.subList(0, steps)) {
            val (x, y) = line.split(",").map { it.toInt() }
            memory.set(x, y, "#")
        }
        val notAllowed = memory.find("#").toSet()
        val path = memory.findPath(memory.topLeft(), memory.bottomRight(), notAllowed, diagonal = false)
        println(path.size - 1)
    }

    override fun solve2(lines: List<String>) {
        val memorySize = if (lines.size > 100) 71 else 7

        val memory = MatrixString.buildDefault(memorySize, memorySize, ".")
        val notAllowed = mutableSetOf<Point>()
        var lastByte = ""
        for (line in lines) {
            val (x, y) = line.split(",").map { it.toInt() }
            val point = Point(x, y)
            memory.set(point, "#")
            lastByte = "$x,$y"
            notAllowed.add(point)

            val path = memory.findPath(memory.topLeft(), memory.bottomRight(), notAllowed, diagonal = false)
            if (path.size == 0) {
                break
            }
        }
        println(lastByte)
    }
}
