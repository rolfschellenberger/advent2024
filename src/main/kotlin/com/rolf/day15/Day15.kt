package com.rolf.day15

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val (w, m) = groupLines(lines, "")
        val warehouse = MatrixString.build(splitLines(w))
        val moves = splitLine(m.joinToString(""))
        var robot = warehouse.find("@").first()

        for (move in moves) {
            robot = move(warehouse, robot, move)
        }

        // Now calculate each box GPS position (O)
        val boxes = warehouse.find("O")
        val sum = boxes.sumOf { box ->
            box.y * 100 + box.x
        }
        println(sum)
    }

    private fun move(
        warehouse: MatrixString,
        robot: Point,
        move: String,
    ): Point {
        val direction = getDirection(move)

        // Look for the edge or first empty space
        val path = mutableListOf(robot)
        var edge = robot
        var movable = false
        while (warehouse.get(edge) != "#" && warehouse.get(edge) != ".") {
            edge = warehouse.getForward(edge, direction) ?: break
            if (warehouse.get(edge) != "#") path.add(edge)
            if (warehouse.get(edge) == ".") movable = true
        }

        // No move
        if (!movable) {
            return robot
        }

        // Now move all objects on the path if possible
        for (i in path.size - 1 downTo 1) {
            val into = path[i]
            val from = path[i - 1]
            warehouse.set(into, warehouse.get(from))
        }
        warehouse.set(robot, ".")
        return path[1]
    }

    private fun getDirection(move: String): Direction {
        return when (move) {
            "^" -> Direction.NORTH
            "v" -> Direction.SOUTH
            "<" -> Direction.WEST
            ">" -> Direction.EAST
            else -> throw IllegalArgumentException("Invalid move: $move")
        }
    }

    override fun solve2(lines: List<String>) {
        val (w, m) = groupLines(lines, "")
        val warehouse = MatrixString.build(splitLines(w))
        val moves = splitLine(m.joinToString(""))
        val newWarehouse = Warehouse.build(warehouse)

        for (move in moves) {
            newWarehouse.move(getDirection(move))
        }

        // Now calculate each box GPS position
        val sum = newWarehouse.boxes.sumOf { box ->
            box.left.y * 100 + box.left.x
        }
        println(sum)
    }
}
